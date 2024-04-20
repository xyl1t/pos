package pkgMain;

public class OuterClass {
	private int x = 99;
    public OuterClass() {
        System.out.println("constructor...x=" + x);
    }
    @Override
    public String toString() {
    	x++;
        return "toString() in OuterClass with x= " + x;
    }
    public class InnerClass {
        private int y = 101;
        /* static not allowed */
        public void incX() {
            x++; 				// access outer class attribute
            System.out.println("[inner class inc x] x+y=" + x + "+" + y);
        }
        public String toString() {
            return "toString() in InnerClass with y= " + y;
        }
    }
}
