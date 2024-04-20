package pkgMain;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("=...new OuterClass...");
        OuterClass oc = new OuterClass();
        System.out.println("=...new InnerClass...");
        OuterClass.InnerClass ic = oc.new InnerClass();
        System.out.println("=...2nd new InnerClass...");
        OuterClass.InnerClass iic = new OuterClass().new InnerClass();
        System.out.println("=...2nd new OuterClass...");
        OuterClass ooc = new OuterClass() {
            @Override
            public String toString() {
                return "toString of ooc";
            }
        };
        System.out.println("---------------------------");
        System.out.println("=...toString Outerclass...");
        System.out.println("=" + oc.toString() + "," + oc.getClass());
        System.out.println("=...toString Innerclass...");
        ic.incX();
        System.out.println("= " + ic.toString() + "," + ic.getClass());
        System.out.println("=...toString 2nd new Outerclass...");
        System.out.println("=" + ooc.toString() + "," + ooc.getClass());
        System.out.println("=" + (ic instanceof OuterClass.InnerClass?"ic is instance of Outer.InnerClass":"is not"));
        System.out.println("=" + (ooc instanceof OuterClass?"ooc is also instance of OuterClass":"is not"));
        //Syntax because InnerClass IS NOT subclass of OuterClass! 
        //System.out.println(ic instanceof OuterClass?"is instance":"is not");
    }
}
