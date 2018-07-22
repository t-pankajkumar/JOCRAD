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

public class Main {

	public native String OCRAD_version();

	public native long OCRAD_open();

	public native int OCRAD_close(long ocrdes);

	public native int OCRAD_get_errno(long ocrdes);

	public native int OCRAD_set_image_from_file(long ocrdes, String filename, boolean invert);

	public native int OCRAD_recognize(long ocrdes, boolean layout);

	public native int OCRAD_result_blocks(long ocrdes);

	public native int OCRAD_result_lines(long ocrdes, int blocknum);

	public native String OCRAD_result_line(long ocrdes, int blocknum, int linenum);

}