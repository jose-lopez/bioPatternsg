 /*
    IteradorCombinacion.java


    Copyright (C) 2016.
    Yackson Ramirez (yacson.ramirez), Jose Lopez (jlopez@unet.edu.ve).

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>

*/

package pipeline;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteradorCombinacion implements Iterable<List<String>> {
	private List<String> lista;
	private Integer k;

	public IteradorCombinacion(List<String> s, Integer k) {
		lista = s;
		this.k = k;
	}

	@Override
	public Iterator<List<String>> iterator() {

		return new IteradorCombn(lista, k);
	}

	private class IteradorCombn implements Iterator<List<String>> {
		private int actualSize, maxresult;
		private Integer curIndex;
		private String[] result;
		private int[] indices;
		private String[] arrayList;
		private List<String> elem = null;

		public IteradorCombn(List<String> s, Integer k) {
			actualSize = k;// desde d�nde
			curIndex = 0;
			maxresult = k;
			arrayList = new String[s.size()];
			for (int i = 0; i < arrayList.length; i++) { // la lista s la vuelca en arrayList
				arrayList[i] = s.get(i);
			}
			this.result = new String[actualSize < s.size() ? actualSize : s.size()]; 
			//el tama�o de result va a ser el valor menor entre actualSize y el tama�o de s
			indices = new int[result.length];

			for (int i = 0; i < result.length; i++) {
				indices[i] = result.length - 2 - i;
			}
		}

		public boolean hasNext() {
			elem = null;
			while ((elem == null && curIndex != -1)) {

				indices[curIndex]++;
				if (indices[curIndex] == (curIndex == 0 ? arrayList.length: indices[curIndex - 1])) {
					
					indices[curIndex] = indices.length - curIndex - 2;
					curIndex--;
				} else {

					result[curIndex] = arrayList[indices[curIndex]];
					
					if (curIndex < indices.length - 1)
						curIndex++;
					else {
						elem = new LinkedList<String>();
						for (String s : result) {
							elem.add(s);
						}

					}
				}
			}
			if (elem == null) {
				if (actualSize < maxresult) {
					actualSize++;
					this.result = new String[actualSize < arrayList.length ? actualSize
							: arrayList.length];
					indices = new int[result.length];

					for (int i = 0; i < result.length; i++) {

						indices[i] = result.length - 2 - i;
					}
					curIndex = 0;

					return this.hasNext();
				} else {
					return false;
				}
			} else {
				return true;
			}
		}

		@Override
		public List<String> next() {
			return elem;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}
	}
}
