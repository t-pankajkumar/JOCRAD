/*  Java JNI wrapper for GNU OCRAD (https://www.gnu.org/software/ocrad/)
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class Ocrad {
	static {
		System.loadLibrary("native");
	}

	public static void main(String[] args) {
		ClassLoader loader = Ocrad.class.getClassLoader();
		File file = new File(loader.getResource("test.pbm").getFile());

		Main m = new Main();
		long ptr = m.OCRAD_open();
		System.out.println(m.OCRAD_version());
		// m.OCRAD_set_image_from_file(ptr, file.getPath(), false);
		try {
			BufferedImage image = ImageIO
					.read(file);
			int[] data = new int[image.getWidth() * image.getHeight()];
			int imgarray[] = image.getRGB(0, 0, image.getWidth(), image.getHeight(), data, 0, image.getWidth());
			
			Pixmap pixmap = new Pixmap(Arrays.toString(imgarray), 1575, 73, 0);
			int errorno = m.OCRAD_get_errno(ptr);
			System.out.println(errorno);
			int set_img = m.OCRAD_set_image(ptr, pixmap, false);
			System.out.println(set_img);
			// Get blocks
			if (errorno == 0 && set_img == 0) {
				int rec = m.OCRAD_recognize(ptr, true);
				int blocks = m.OCRAD_result_blocks(ptr);
				System.out.println(blocks);
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

}
