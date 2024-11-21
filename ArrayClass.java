public class ArrayClass<T> {  // T is a placeholder for any object type (e.g., Integer, String, etc.)
    private T[] arr;
    private int size;

    
    public ArrayClass(int size) {
        arr = (T[]) new Object[size];  // Initialize the array with a generic type
        this.size = 0;
    }

    public void add(T value) {
        if (size < arr.length) {
            arr[size] = value;
            size++;
        } else {
            System.out.println("Array is full, cannot add more elements.");
        }
    }

    public void remove(int index) {
        if (index >= 0 && index < size) {
            for (int i = index; i < size - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[size - 1] = null;  // Set the last element to null
            size--;
        } else {
            System.out.println("Invalid index.");
        }
    }

    public T get(int index) {
        if (index >= 0 && index < size) {
            return arr[index];
        } else {
            System.out.println("Index out of bounds.");
            return null;  // Return null if the index is out of bounds
        }
    }

    public int size() {
        return size;
    }

    public void print() {
        if (size == 0) {
            System.out.println("Array is empty.");
        } else {
            for (int i = 0; i < size; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        }
    }
}
