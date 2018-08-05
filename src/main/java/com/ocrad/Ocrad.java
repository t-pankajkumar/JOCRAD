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

import java.io.File;

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
		Pixmap pixmap = new Pixmap(new int[]{0,1,3},100,500,0);
		System.out.println(m.OCRAD_set_image(ptr, pixmap, false));
		m.OCRAD_set_image_from_file(ptr, file.getPath(), false);
		int errorno = m.OCRAD_get_errno(ptr);
		m.OCRAD_recognize(ptr, true);
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
	}

}
