public class Main {
    public static void main(String[] args) {
        var o = new Klass();
        var randomProcessor = new RandomProcessor();
        randomProcessor.processor(o);
        System.out.println(o.getIvalue());
        System.out.println(o.getDice());
        System.out.println(o.getDvalue());
        System.out.println(o.getLvalue());
        System.out.println(o.getName());
    }
}
