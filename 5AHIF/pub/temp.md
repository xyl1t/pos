public class Pub {
  final static private int UNDEFINED = -99;
  private ObjectId _id;
  private String name, location;
  private int founded; // year
  private String description;

  cosntructor
  getter
  setter

  toString() {
    int len = Math.min(description.length(), MAXLEN_OF_DESC 10);
    return name + location + founded + descr.substring(0, len) + " ... ";
  }

  toLongString
}
