
/**
 * Sorting.java contains 4 public static methods for sorting data.
 * @author Vincent Lee
 * @version 1.0
 */

public class Sorting {
	
	/**
	 * Swap Method
	 * @param data Array containing data to be sorted.
	 * @param index1 First position to be examined.
	 * @param index2 Second position to be examined.
	 */

	private static void swap(int[] data, int index1, int index2) {
		int temp = data[index1];
		data[index1] = data[index2];
		data[index2] = temp;
	}
	
	/**
	 * Partition Method
	 * @param data Array containing data to be sorted.
	 * @param min Minimum number to be searched.
	 * @param max Maximum number to be searched.
	 * @return Right number.
	 */
	
	private static int partition(int[] data, int min, int max) {
		// Use first element as the partition value
		int partitionValue = data[min];
		int left = min;
		int right = max;
		while (left < right) {
			// Search for an element that is > the partition element
			while (data[left] <= partitionValue && left < right)
				left++;
			// Search for an element that is < the partition element
			while (data[right] > partitionValue)
				right--;
			if (left < right)
				swap(data, left, right);
		}
		
		// Move the partition element to its final position
		swap (data, min, right);
		return right;
	}
	
	/**
	 * Insertion Sort Method
	 * @param data Array containing data to be sorted.
	 */
	
	public static void insertionSort(int[] data) {
		for (int index = 1; index < data.length; index++) {
			int key = data[index];
			int position = index;
			
			// Shift larger values to the right
			while (position > 0 && (data[position-1] > key)) {
				data[position] = data[position-1];
				position--;
			}
			
			data[position] = key;
		}
	}
	
	/**
	 * Bubble Sort Method
	 * @param data Array containing data to be sorted.
	 */
	
	public static void bubbleSort(int[] data) {
		int position, scan;
		for (position = data.length - 1; position >= 0; position--) {
			for (scan = 0; scan <= position - 1; scan++) {
				if (data[scan] > data[scan+1])
					swap (data, scan, scan+1);
			}		
		}
	}

	/**
	 * Happy Hour Sort or Cocktail Sort
	 * @param data Array containing data to be sorted.
	 * @param max Length of the ArrayList containing data to be sorted.
	 */
	
	public static void happyHourSort(int[] data, int max) {
		int counter = -1;
		
		while (counter <  max) {
			counter++;
			max--;
			for (int j = counter; j <  max; j++) {
				if (data[j] > data[j+1]) {
					int T = data[j];
					data[j] = data[j+1];
					data[j+1] = T;
				}
			}
			for (int j = max; --j >= counter;) {
				if (data[j] > data[j+1]) {
					int T = data[j];
					data[j] = data[j+1];
					data[j+1] = T;
				}
			}
		}
	}
	
	/**
	 * Quick Sort Method
	 * @param data Array containing data to be sorted.
	 * @param min Lowest Number in the ArrayList to be sorted.
	 * @param max Length of the ArrayList containing data to be sorted.
	 */
	
	public static void quickSort(int[] data, int min, int max) {
		int pivot;
		if (min < max) {
			pivot = partition(data, min, max); // make partitions
			quickSort(data, min, pivot-1); // sort left partition
			quickSort(data, pivot+1, max); // sort right partition
		}
	}
}
