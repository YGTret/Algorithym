
public class RBTreeStart {
    public static void main(String[] args) {
        RBTree<Integer, String> tree = new RBTree<>();

        tree.insert(8, "8");
        tree.insert(13, "13");
        tree.insert(128, "128");
        tree.insert(5, "5");
        tree.insert(9, "9");
        tree.insert(64, "64");
        tree.insert(25, "25");

        System.out.println("Минимальное значение: " + tree.min());
        System.out.println("Максимальное значение: " + tree.max());

        System.out.println("Удаление минимального значения...");
        tree.pop_back();
        System.out.println("Минимальное значения после удаления: " + tree.min());

        System.out.println("Удаление максимального значения...");
        tree.pop_front();
        System.out.println("Максимальное значения после удаления: " + tree.max());

        System.out.println("Получение значения по ключу 3: " + tree.get(3));
        System.out.println("Содержит ключ 15: " + tree.contains(15));

        System.out.println("Удаление ключа 5...");
        tree.remove(5);
        System.out.println("Содержит ключ 5 после удаления " + tree.contains(5));
    }
}
