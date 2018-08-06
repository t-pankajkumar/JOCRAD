/*	Java JNI wrapper for GNU OCRAD (https://www.gnu.org/software/ocrad/)
    Copyright (C) 2018  T Pankaj Kumar.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.ocrad;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

public class Ocrad {
	static {
		System.loadLibrary("native");
	}

	public static void main(String[] args) {
		ClassLoader loader = Ocrad.class.getClassLoader();
		File file = new File(loader.getResource("test.pbm").getFile());

		try {
			Main m = new Main();
			long ptr = m.OCRAD_open();
			System.out.println(m.OCRAD_version());
			BufferedImage myPicture;
			myPicture = ImageIO
					.read(new File("Image.png"));
			//System.out.println(ppm(myPicture));
			Pixmap pixmap = new Pixmap(ppm(myPicture), 73, 1575, 0);
			System.out.println(m.OCRAD_set_image(ptr, pixmap, false));
			//m.OCRAD_set_image_from_file(ptr, file.getPath(), false);
			int errorno = m.OCRAD_get_errno(ptr);
			m.OCRAD_recognize(ptr, true);
			System.out.println(errorno);
			// Get blocks
			if (errorno == 0) {
				int blocks = m.OCRAD_result_blocks(ptr);
				int linenum;
				for (int i = 0; i < blocks; i++) {
					linenum = m.OCRAD_result_lines(ptr, i);

					for (int j = 0; j < linenum; j++) {
						 System.out.println(m.OCRAD_result_line(ptr, i, j));
					}
				}
			}

			m.OCRAD_close(ptr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int[] pbm(BufferedImage image) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, false);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(".pbm: couldn't grab pixels from image !");
		}
		int bpl = ((width % 8) == 0) ? width >> 3 : (width + 8) >> 3; // bytes per line
//	    String header="P4\n"+(bpl<<3)+" "+height+"\n";
		String header = "P4\n" + width + " " + height + "\n";
		// System.out.println(header.getBytes());
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		WritableRaster raster = image.getRaster();

		int k = 0;
		byte b;
		int pixel;
		int[] bitmap = new int[bpl * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; (x < width) && (k < bitmap.length);) {
				b = 0; // all white
				for (int bit = 7; (bit >= 0) && (x < width); bit--) {
					pixel = raster.getSample(x++, y, 0);
					if (pixel == 0) {
						b |= (1 << bit);
					} // if black set bit
				}
				bitmap[k++] = b;
				System.out.println((int) b & 0xffb);
			}
		}
		/*
		 * for (int i = 0; i < bitmap.length; i++) sb.append((int) bitmap[i] & 0xff);
		 */
		return bitmap;
	}

	public static byte[] ppm(BufferedImage image) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		int data[] = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, data, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(".ppm: couldn't grab pixels from image !");
		}

		String header = "P6\n" + width + " " + height + "\n255\n";

		ColorModel model = grabber.getColorModel();

		int k = 0;
		int col;
		byte[] bitmap = new byte[width * height * 3];
		/*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Buff.ppm")));
		StringBuilder sb = new StringBuilder();*/
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				col = data[y * width + x];
				bitmap[k++] = (byte) model.getRed(col);
				bitmap[k++] = (byte) model.getGreen(col);
				bitmap[k++] = (byte) model.getBlue(col);
				//byte[] b = { (byte) model.getRed(col), (byte)model.getGreen(col), (byte)model.getBlue(col) };
				//System.out.println( new String(b,"ISO-8859-1") );
				/*writer.write(new String(b,"ISO-8859-1"));
				sb.append(new String(b,"ISO-8859-1"));*/
			}
		}
		//writer.flush();
		//writer.close();
		//System.out.println(sb.toString().length());
		/*for(int i=0;i<bitmap.length;i++)
			System.out.println(bitmap[i]);*/
		return bitmap;
	}

}
