package algrm;

import java.awt.image.*;

public class HistogramEqualization {
	
	BufferedImage origImg, eqImg, greyImg;
	short[] rPix, gPix, bPix;
	short[] avgGreyPix, avgEqPix;
	int[] origPixels;
	
	double[] normalizedHist;
	short[] eqHist;
	int[] hist;
	
	int rows, cols;	
	void init(){
		origPixels = new int[rows*cols];
		rPix = new short[rows*cols];
		gPix = new short[rows*cols];
		bPix = new short[rows*cols];
		
		avgGreyPix = new short[rows*cols];
		avgEqPix = new short[rows*cols];
		
		hist = new int[256];
		normalizedHist = new double[256];
		eqHist = new short[256];		
		
		greyImg = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		eqImg = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
	}
	
	public HistogramEqualization(BufferedImage bufImg) {
		origImg = bufImg;
		
		rows = origImg.getHeight();
		cols = origImg.getWidth();
		init();
		
		origPixels = origImg.getRGB(0, 0, cols, rows, null, 0, cols);
		
		unpackRGBpixels(origPixels, rPix, gPix, bPix);		
		
		greyImg = getGreyImage();
		
		eqImg = getEqualizedImage();
	}

	BufferedImage getGreyImage(){
		
		for(int index=0; index < rows*cols; index++){
				
			//avgGreyPix[index] =  (short) ((short)(rPix[index] + gPix[index] + bPix[index])/3);
			avgGreyPix[index] =  (short)(rPix[index]*0.299 + gPix[index]*0.587 + bPix[index]*0.144);
				if(avgGreyPix[index] > 255)
					avgGreyPix[index] = 255;
		}
		
		return greypix2Image(avgGreyPix);
	}

	BufferedImage greypix2Image(short[] greyPixels){
		
		BufferedImage greyedImage = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		
		int[] imagePixels = new int[rows * cols];
		
		packRGBpixels(imagePixels, greyPixels, greyPixels, greyPixels);		
		
		int index = 0;		
			for(int r=0; r<rows; r++)
				for(int c=0; c<cols; c++)
				greyedImage.setRGB(c, r, imagePixels[index++]); 

        return greyedImage;
		
	}
	BufferedImage getEqualizedImage(){
		
		doHistEqualize(avgGreyPix, normalizedHist, eqHist);
		
		for(int index=0; index < rows * cols; index++)
			avgEqPix[index] = (eqHist[avgGreyPix[index]]);
		
				
		return greypix2Image(avgEqPix);
	}

	void doHistEqualize(short[] avgGreyPix, double[] normalizedHist, short[] equalizedHist){
		
		for(int i=0; i<avgGreyPix.length; i++){
			hist[avgGreyPix[i]]++;
		}		
		
		double sum=0;
		for(int l=0; l < 256; l++){
			//normalize 
			normalizedHist[l] = hist[l]/(rows*cols);
			//equalize
			sum += hist[l];
			equalizedHist[l] =  (short) (sum*255/(rows*cols));
		}
	}

	void unpackRGBpixels(int[] pixels, short[] rPix, short[] gPix, short[] bPix){
		
		for(int i=0; i<pixels.length; i++){
			rPix[i] = (short)bitShiftRt(pixels[i], 16);
			gPix[i] = (short)bitShiftRt(pixels[i], 8);
			bPix[i] = (short)bitShiftRt(pixels[i], 0);
		}
		
	}
	void packRGBpixels(int[] pixels, short[] rPix, short[] gPix, short[] bPix){
		
		for(int i=0; i<pixels.length; i++){
			
			pixels[i] = bitShiftLt(rPix[i], 16) |
					  bitShiftLt(gPix[i], 8) |
					  bitShiftLt(bPix[i], 0);
		}
	}
	
	int bitShiftRt(int px, int mode)
	{
		int pxShift = 0;
		switch(mode){
		
			case 16: pxShift = (px & 0x00FF0000) >>> 16; break;
			case 8:  pxShift = (px & 0x0000FF00) >>> 8; break;
			case 0:  pxShift = (px & 0x000000FF) >>> 0; break;
			default: break;
		}

		return pxShift;
			
	}
	
	int bitShiftLt(int px, int mode)
	{
		int pxShift = 0;
		switch(mode){
		
			case 16: pxShift = (px & 0x000000FF) << 16; break;
			case 8:  pxShift = (px & 0x000000FF) << 8; break;
			case 0:  pxShift = (px & 0x000000FF) << 0; break;
			default: break;
		}

		return pxShift;
	}
	
	public BufferedImage getEqImg(){
		if(eqImg != null)
			return eqImg;
		else
			return null;
	}
}
