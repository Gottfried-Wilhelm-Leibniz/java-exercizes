public class Main {
    public static void main(String[] args) {
        var a = Integer.valueOf(7);
        var b = Integer.valueOf(8);
        var c = Integer.valueOf(4);
        var d = Integer.valueOf(9);
        var e = Integer.valueOf(10);
        var cont = new ContainerX<Integer>();
        cont.push(a);
        cont.push(b);
        cont.push(c);
        cont.push(d);
        cont.push(e);

        System.out.println("min " + cont.min());
        System.out.println(cont.pop());
        System.out.println(cont.min());
        System.out.println(cont.pop());
        System.out.println(cont.min());
        System.out.println(cont.pop());
    }

}
