public interface List<T> {
    void findFirst();
    void findNext();
    T retrieve();
    void insert(T value);
    boolean empty();
    boolean last();
}
