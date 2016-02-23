package dao;

public class SortField {
  private String field;
  private boolean dir;

  public SortField(String field, boolean dir) {
    this.field = field;
    this.dir = dir;
  }

  public String getField() {
    return field;
  }

  public boolean isAsc() {
    return dir;
  }
}
