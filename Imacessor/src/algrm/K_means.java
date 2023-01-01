package algrm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class K_means {

	public BufferedImage image;
	int K;
	int width;
	int height;
	int index;	
	int[] pixelsRGB;
	int[] prevCent, currCent;
	boolean fakeColor;
	
	public K_means(BufferedImage img, int kValue, boolean isfakeColor){

		this.fakeColor = isfakeColor;
		
		width = img.getWidth();
		height = img.getHeight();
		
		this.image = new BufferedImage(width, height, img.getType());
		Graphics2D g = image.createGraphics();
		g.drawImage(img, 0, 0, width, height, null );		  

		this.K = kValue;		
		
		pixelsRGB = new int[width*height];
		index = 0;
		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
				pixelsRGB[index++] = image.getRGB(i, j); 
		
		prevCent = new int[K];
		currCent = new int[K];
		setZeros(prevCent);
		setZeros(currCent);
		
		// kmeans operation
		KmeansAlg(pixelsRGB);	

				
	}

	void KmeansAlg(int[] pix){
		
		double minDist = Double.MAX_VALUE;
		double distance = 0;
		int clusterIndex = -1;
		int[] pixClusterIndex = new int[pix.length]; 
		int[] pixNumCluster = new int[K];
		int[] RvalCluster = new int[K];
		int[] GvalCluster = new int[K];
		int[] BvalCluster = new int[K];
		
		for(int j=0; j < K; j++){
			Random rand = new Random();
			currCent[j] = pix[rand.nextInt(pix.length)];
			System.out.println(currCent[j]);
		}
		
		int iterations = 5000;
		int iter;
		for(iter=0; iter < iterations; iter++){
			
			for(int j=0; j < K; j++){
				prevCent[j] = currCent[j];
				pixNumCluster[j] = 0;
				RvalCluster[j] = 0;
				GvalCluster[j] = 0;
				BvalCluster[j] = 0;
			}
			
			for(int i=0; i < pix.length; i++)
			{
				minDist = Double.MAX_VALUE;
				
				for(int j=0; j < K; j++){
					distance = distRGB(pix[i], currCent[j]);

					if(distance < minDist){
						minDist = distance;
						clusterIndex = j;
					}
				}
				
				pixClusterIndex[i] = clusterIndex;
				
				pixNumCluster[clusterIndex]++;
				RvalCluster[clusterIndex] += bitShiftRt(pix[i], 16);
				GvalCluster[clusterIndex] += bitShiftRt(pix[i], 8);
				BvalCluster[clusterIndex] += bitShiftRt(pix[i], 0);
			}			
			
			for(int j=0; j < K; j++){
				
				int Rval_avg = (int)((double)RvalCluster[j] / (double)pixNumCluster[j]);
				int Gval_avg = (int)((double)GvalCluster[j] / (double)pixNumCluster[j]);
				int Bval_avg = (int)((double)BvalCluster[j] / (double)pixNumCluster[j]);
				
				currCent[j] = bitShiftLt(Rval_avg, 16) | 
								bitShiftLt(Gval_avg, 8) | 
								bitShiftLt(Bval_avg, 0); 
                       
			}
			
			if(isConverged(prevCent, currCent))
				break;
		}

		System.out.println("Iterations: " + iter);
		
	    for ( int i = 0; i < pix.length; i++ ) {
	        pix[i] = currCent[pixClusterIndex[i]];
	      }
	    
	    // basic 14 Colors
	    int numColor = 14;
	    if(K <= numColor && fakeColor){ 	

		    int[] Colors = new int[] {
		    	0xFF0000, //Red (255,0,0)
		    	0x00FF00, //Lime (0,255,0)
		    	0X0000FF, // Blue (0, 0, 255)
		    	0xFFFF00, // Yellow (255,255,0)
		    	0x00FFFF, // Cyan (0,255,255)
		    	0xFF00FF, // Magenta (255,0,255)
		    	0xC0C0C0, // Silver (192,192,192)
		    	0x808080, // Gray (128,128,128)
		    	0x800000, // Maroon (128,0,0)
		    	0x808000, // Olive (128,128,0)
		    	0x008000, // Green (0,128,0)
		    	0x800080, // Purple (128,0,128)
		    	0x008080, // Teal (0,128,128)
		    	0x000080  // Navy (0,0,128)		    	
		    };
		    
		    for ( int i = 0; i < pix.length; i++ ) {
		        pix[i] = Colors[pixClusterIndex[i]];
		      }
		    
		    		    
	    }  else {
		    for ( int i = 0; i < pix.length; i++ ) {
		        pix[i] = currCent[pixClusterIndex[i]];
		      }
	    }
/*	    for ( int j=0; j < K; j++){
	        System.out.println(currCent[j]);
	      }*/
		
	}
	double distRGB(int px1, int px2)
	{
		int distR = pixDist(px1, px2, 16);
		int distG = pixDist(px1, px2, 8);
		int distB = pixDist(px1, px2, 0);
		
		return Math.sqrt(distR + distG + distB);
	}
	int pixDist(int px1, int px2, int mode){
		
		int p1 = bitShiftRt(px1, mode);
		int p2 = bitShiftRt(px2, mode);
		
		return (p1-p2)*(p1-p2);
				
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
	boolean isConverged(int[] prevCent, int[] currCent){
		for(int j=0; j<currCent.length; j++){
			if(prevCent[j] != currCent[j])
				return false;
		}
		return true;
	}
	
	void setZeros(int[] arr){
		for(int i=0; i<arr.length; i++)
			arr[i] = 0;
	}
	public BufferedImage getKmeansImage(){
		
		index = 0;
		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
				image.setRGB(i, j, pixelsRGB[index++]); 
		
		return image;
	}
	
	

}
