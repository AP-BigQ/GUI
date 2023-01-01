package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.Pipe;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/*------------Package included ----------*/
import algrm.*;

public class ImageFrame extends JFrame {

	Dimension screenSize;
	int centerX, centerY;

	JScrollPane scPane, scPane2;
	JSplitPane imageScPanes;

	ImagePanel imgPanel;
	ImageLabel imgLabel;

	Image img;
	BufferedImage bufImg = null, OrigImg;
	JMenuBar menuBar;
	JMenu fileMenu, aboutMenu, preProcessMenu, filteringMenu, classfiMenu,
			registerMenu, segmentationMenu;
	final static int Width = 900;
	final static int Height = 750;
	JTextArea log;
	JList imagesList;
	DefaultListModel listModel;
	StringBuilder dirPath;
	boolean loadImgOK = false;
	JToolBar toolbar;

	public ImageFrame() {
		super();
		setTitle("Imacessor");
		setSize(Width, Height);

		init();
	}

	void init() {
		Container contentPane = this.getContentPane();

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getWidth();
		int h = getHeight();
		centerX = screenSize.width / 2 - w / 2;
		centerY = screenSize.height / 2 - h / 2;
		setLocation(centerX, centerY);

		// setLayout( new BorderLayout() );

		imgPanel = new ImagePanel();
		imgLabel = new ImageLabel();

		scPane = new JScrollPane(imgPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPane2 = new JScrollPane(imgLabel);

		scPane.setOpaque(true);
		scPane2.setOpaque(true);

		listModel = new DefaultListModel();

		imagesList = new JList(listModel);
		imagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imagesList.setSelectedIndex(0);

		imagesList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				change2SelectedFile();
			}
		});

		imagesList.setVisibleRowCount(50);
		imagesList.setBackground(new Color(240, 255, 255));

		JScrollPane listScPane = new JScrollPane(imagesList);
		listScPane.setPreferredSize(new Dimension(w / 6, h));

		log = new JTextArea(10, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		log.setBackground(new Color(240, 255, 255));
		JScrollPane logScPane = new JScrollPane(log);
		logScPane.setPreferredSize(new Dimension(w, h / 15));

		toolbar = new JToolBar();
		toolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		toolbar.add(new JButton("Brush ?")).setEnabled(false);
		toolbar.add(new JButton("Palette ")).setEnabled(false);
		toolbar.add(new JButton("Colors ")).setEnabled(false);
		toolbar.addSeparator();
		// toolbar.add(new JButton("About"));

		imageScPanes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scPane,
				scPane2);
		imageScPanes.setResizeWeight(0.5);

		imageScPanes.setContinuousLayout(true);
		imageScPanes.setOneTouchExpandable(true);

		contentPane.add(toolbar, BorderLayout.PAGE_START);

		contentPane.add(listScPane, BorderLayout.LINE_START);

		contentPane.add(imageScPanes, BorderLayout.CENTER);

		contentPane.add(logScPane, BorderLayout.PAGE_END);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem loadImageItem = new JMenuItem("Load Image");
		loadImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				loadImage();
			}
		});
		fileMenu.add(loadImageItem);

		JMenuItem loadImageFolderItem = new JMenuItem("Load Image Folder");
		loadImageFolderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				loadImageFolder();
			}
		});
		fileMenu.add(loadImageFolderItem);

		JMenuItem saveImageItem = new JMenuItem("Save Image");
		saveImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveImage();
			}
		});
		fileMenu.add(saveImageItem);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		preProcessMenu = new JMenu("PreProcessing");
		JMenuItem hItem = new JMenuItem("Histogram");
		hItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (bufImg == null) {
					JOptionPane.showMessageDialog(null, "Open File First");
					return;
				}

				hist();
				log.append("showing histogram" + "." + "\n");
			}
		});
		preProcessMenu.add(hItem);

		filteringMenu = new JMenu("Filters");
		menuBar.add(preProcessMenu).add(filteringMenu);

		// Gaussian
		JMenuItem GItem = new JMenuItem("Gaussian Filter");
		GItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (bufImg == null) {
					JOptionPane.showMessageDialog(null, "Open File First");
					return;
				}

				int radius = 3;
				GF(radius);
				log.append("Applying Gaussian Filter" + "." + "\n");
			}

		});
		filteringMenu.add(GItem);

		// Median
		JMenuItem MItem = new JMenuItem("Median Filter");
		MItem.setEnabled(false);
		MItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (bufImg == null) {
					JOptionPane.showMessageDialog(null, "Open File First");
					return;
				}

				// MF();
			}

		});
		filteringMenu.add(MItem);

		JMenuItem edgeDetectItem = new JMenuItem("Edge Detect");
		edgeDetectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f,
						0.0f, -1.0f, 0.0f };
				convolve(elements);
			}
		});
		preProcessMenu.add(edgeDetectItem);

		segmentationMenu = new JMenu("Segmentation");
		menuBar.add(segmentationMenu);

		// k-means clustering
		JMenuItem kItem = new JMenuItem("K-means clustering");
		kItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (bufImg == null) {
					JOptionPane.showMessageDialog(null, "Open File First");
					return;
				}

				int k = doEdit(4);
				boolean isfakeColor = EditDialg.fakeColor;

				K_means kmeansObj = new K_means(bufImg, k, isfakeColor);
				bufImg = kmeansObj.getKmeansImage();

				repaint();
				log.append("Applying K-means clustering" + "." + "\n");
			}

		});
		segmentationMenu.add(kItem);

		// watershed segmentation
		JMenuItem WItem = new JMenuItem("Watershed");
		WItem.setEnabled(false);
		WItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}

		});
		segmentationMenu.add(WItem);

		// Otsu's method
		JMenuItem OItem = new JMenuItem("Otsu's");
		OItem.setEnabled(false);
		OItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}

		});
		segmentationMenu.add(OItem);

		// texture filters
		JMenuItem TItem = new JMenuItem("Texture filters");
		TItem.setEnabled(false);
		TItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}

		});
		segmentationMenu.add(TItem);

		// EM
		JMenuItem EItem = new JMenuItem("EM");
		EItem.setEnabled(false);
		EItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}

		});
		segmentationMenu.add(EItem);

		// Image Classification
		classfiMenu = new JMenu("Classification");
		menuBar.add(classfiMenu);
		JMenuItem kmeansItem = new JMenuItem("K-Means");
		kmeansItem.setEnabled(false);
		kmeansItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (bufImg == null) {
					JOptionPane.showMessageDialog(null, "Open File First");
					return;
				}

				repaint();
			}
		});
		classfiMenu.add(kmeansItem);

		// Image Registration
		registerMenu = new JMenu("Registration");
		menuBar.add(registerMenu);
		// Point Mapping
		JMenuItem PItem = new JMenuItem("Point Mapping");
		PItem.setEnabled(false);
		PItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("PM");
			}

		});
		registerMenu.add(PItem);

		// About
		aboutMenu = new JMenu("Help");
		JMenuItem AItem = new JMenuItem("About");
		aboutMenu.add(AItem);

		AItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				popupAbout();
			}
		});
		menuBar.add(aboutMenu);

		setVisible(true);
		// pack();

		repaint();
	}

	void popupAbout() {
		JDialog diag = new JDialog(this, "About", true);

		diag.setSize(70, 80);
		diag.setLocationRelativeTo(this);
		diag.add(new JLabel("  Imacessor 2016"), BorderLayout.CENTER);
		diag.setIconImage(bufImg);
		diag.setResizable(false);
		diag.setVisible(true);
	}

	void loadImage() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select a File");
		String[] extensions = ImageIO.getReaderFileSuffixes();
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files",
				extensions));
		int r = chooser.showOpenDialog(this);
		if (r != JFileChooser.APPROVE_OPTION)
			return;

		// try{

		dirPath = new StringBuilder(chooser.getSelectedFile().getParent());

		if (!listModel.contains(chooser.getSelectedFile().getName())) {
			loadImgOK = true;
			listModel.addElement(chooser.getSelectedFile().getName());
		} else
			return;

		// log.append("Open: " + chooser.getSelectedFile() + "." + "\n");

		if (listModel.getSize() != 0)
			imagesList.setSelectedIndex(listModel.getSize() - 1);
		else
			imagesList.setSelectedIndex(0);

		// }
		// catch(IOException e) {JOptionPane.showMessageDialog(this, e);}

		// imgPanel.paint(getGraphics());
		// imgLabel.paint(getGraphics());
	}

	void loadImageFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select a Folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		String[] extensions = ImageIO.getReaderFileSuffixes();
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files Folder",
				extensions));

		final String[] fileSuffix = { ".png", ".jpg", "jpeg", "tif"};

		FilenameFilter imgFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				for (int i = 0; i < fileSuffix.length; i++)
					if (lowercaseName.endsWith(fileSuffix[i]))
						return true;
				return false;
			}
		};

		int r = chooser.showOpenDialog(this);
		if (r == JFileChooser.APPROVE_OPTION) {

			try {
				File[] filesInDir = chooser.getSelectedFile().listFiles(
						imgFilter);
				for (File file : filesInDir) {
					if (!listModel.contains(file.getName()))
						listModel.addElement(file.getName());
				}

				dirPath = new StringBuilder(chooser.getSelectedFile()
						.getCanonicalPath());

				log.append("Open Folder: " + chooser.getSelectedFile() + "."
						+ "\n");

				if (listModel.getSize() != 0)
					imagesList.setSelectedIndex(listModel.getSize() - 1);
				else
					imagesList.setSelectedIndex(0);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e);
			}
		}

		repaint();

	}

	void change2SelectedFile() {

		StringBuilder filePath = new StringBuilder(dirPath);
		filePath.append(File.separator);
		filePath.append(imagesList.getSelectedValue());

		File selectFile = new File(filePath.toString());

		try {

			img = ImageIO.read(selectFile);
			bufImg = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			bufImg.getGraphics().drawImage(img, 0, 0, null);

			OrigImg = bufImg;

			log.append("Open File: " + selectFile + "." + "\n");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e);
		}

		repaint();

	}

	void saveImage() {
		if (bufImg == null) {
			JOptionPane.showMessageDialog(this, "Open File First");
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		String[] extensions = ImageIO.getReaderFileSuffixes();
		// String[] extensions = ImageIO.getWriterFileSuffixes();
		chooser.setFileFilter(new FileNameExtensionFilter("Image Files",
				extensions));
		int r = chooser.showSaveDialog(this);
		if (r != JFileChooser.APPROVE_OPTION)
			return;
		try {
			ImageIO.write(bufImg, "png", chooser.getSelectedFile());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e);
		}

	}

	void convolve(float[] elements) {
		Kernel kernel = new Kernel((int) Math.sqrt(elements.length),
				(int) Math.sqrt(elements.length), elements);
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

		System.out.println((int) Math.sqrt(elements.length) + "X"
				+ (int) Math.sqrt(elements.length) + " Kernel");
		filter(op);
	}

	void filter(BufferedImageOp op) {
		if (bufImg == null)
			return;
		bufImg = op.filter(bufImg, null);
		repaint();

	}

	void hist() {
		HistDialog hdiag = new HistDialog(this, bufImg);

	}

	void GF(int radius) {
		// Horizontal Gaussian
		Kernel kernel = GSKernel(radius, true);
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		filter(op);
		// Vertical Gaussian -- 2D Gaussian
		kernel = GSKernel(radius, false);
		op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		filter(op);
	}

	Kernel GSKernel(int radius, boolean horVer) {

		int length = 2 * radius + 1;
		float[] elements = new float[length];
		float sigma = radius / 3.0f;
		float sigma22 = 2 * sigma * sigma;
		float sigmaPi2 = (float) ((float) 2.0f * Math.PI * sigma);
		float sqrtSigmaPi2 = (float) Math.sqrt(sigmaPi2);
		float radius2 = radius * radius;
		float total = 0;

		for (int i = -radius; i <= radius; i++) {
			float dist = i * i;
			int index = i + radius;
			if (dist > radius2)
				elements[index] = 0;
			else
				elements[index] = (float) Math.exp(-dist / sigma22)
						/ sqrtSigmaPi2;
			total += elements[index];
		}
		for (int i = 0; i < length; i++) {
			elements[i] /= total;
		}

		if (horVer) {
			return new Kernel(length, 1, elements);
		} else {
			return new Kernel(1, length, elements);
		}

	}

	public class ImagePanel extends JPanel {
		private PanZoomListener panzoom = new PanZoomListener(this);

		private boolean init = true;
		Point2D pt0;
		int pt0X, pt0Y;

		public ImagePanel() {
			super();
			// panzoom = new PanZoomListener(this); NOT correct before Object
			// initialization

			this.addMouseListener(panzoom);
			this.addMouseMotionListener(panzoom);
			this.addMouseWheelListener(panzoom);
		};

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			if (bufImg == null)
				return;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			if (imgPanel != null && bufImg != null) {

				if (init) {
					panzoom.setTransfm(g2.getTransform());
					init = false;

					try {
						pt0 = panzoom.getTransfm().inverseTransform(
								new Point(0, 0), null);
					} catch (NoninvertibleTransformException e) {
						e.printStackTrace();
					}

				} else {
					g2.setTransform(panzoom.getTransfm());
				}

				if (imgPanel != null && bufImg != null) {
					int x = (scPane.getWidth() - bufImg.getWidth()) / 2;
					int y = (scPane.getHeight() - bufImg.getHeight()) / 2;

					FontMetrics fm = g.getFontMetrics();
					Rectangle2D rect = fm.getStringBounds("Processing Image",
							g2);
					setFont(new Font("default", Font.BOLD, 12));
					/*
					 * g2.drawString("Processing Image", (int) (getWidth()/2 -
					 * rect.getWidth()/2), (int) (y - rect.getHeight()/2));
					 */

					// g2.drawString("(0,0)", 0, 0);

					pt0X = (int) (pt0.getX());
					pt0Y = (int) pt0.getY();

					g2.drawImage(bufImg, x + pt0X, y + pt0Y, null);
					/*
					 * g2.drawString("Processing Image", (int) (x-
					 * rect.getWidth()/2), (int) (tmpY - rect.getHeight()/2));
					 */

					setPreferredSize(new Dimension(bufImg.getWidth(),
							bufImg.getHeight()));

				}

			}

			/*
			 * // Draw the target lines g2.drawLine(-2000, 0+getHeight()/2 +
			 * pt0Y, 2000, 0+getHeight()/2 + pt0Y); g2.drawLine(0+getWidth()/2+
			 * pt0X, -2000, 0+getWidth()/2+ pt0X, 2000); double RAD = 3200.0;
			 * int N = 80; for(int i=0; i<=N; i++) { int rad = (int)(RAD/N)*i;
			 * g2.drawOval(-rad+getWidth()/2+ pt0X, -rad+getHeight()/2 + pt0Y,
			 * 2*rad, 2*rad); }
			 */

			revalidate();

		}

	}

	public class ImageLabel extends JLabel {

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (OrigImg != null) {
				int x = (getWidth() - OrigImg.getWidth()) / 2;
				int y = (getHeight() - OrigImg.getHeight()) / 2;

				FontMetrics fm = g.getFontMetrics();
				Rectangle2D rect = fm.getStringBounds("Original Image", g);
				setFont(new Font("default", Font.BOLD, 12));
				g.drawString("Original Image",
						(int) (getWidth() / 2 - rect.getWidth() / 2),
						(int) (y - rect.getHeight() / 2));

				g.drawImage(OrigImg, x, y, null);
				setPreferredSize(new Dimension(OrigImg.getWidth(),
						OrigImg.getHeight()));
			}

			revalidate();
		}

	}

	int doEdit(int k) {
		EditDialg diag = new EditDialg(this, "Edit K-means parameter", k);
		return diag.getTextValue();
	}

	public class ImageInfo {
		private String name;
		private String path;
		private int width;
		private int height;

		public ImageInfo(File file) throws IOException {
			name = file.getName();
			path = file.getCanonicalPath();
		}

		public String toString() {
			return name;
		}
	}

}
