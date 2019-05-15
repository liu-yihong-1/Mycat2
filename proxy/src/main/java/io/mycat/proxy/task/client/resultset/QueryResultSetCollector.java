package io.mycat.proxy.task.client.resultset;

import io.mycat.beans.mysql.packet.ColumnDefPacket;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author jamie12221
 * @date 2019-05-11 14:44 文本结果集收集类
 **/
public class QueryResultSetCollector implements TextResultSetTransforCollector,
    Iterable<Object[]> {

  protected ArrayList[] result;
  protected ColumnDefPacket[] columns;
  protected int columnCount = 0;

  @Override
  public void addValue(int columnIndex) {
    result[columnIndex].add(null);
  }

  @Override
  public void addValue(int columnIndex, String value) {
    result[columnIndex].add(value);
  }

  @Override
  public void addValue(int columnIndex, long value) {
    result[columnIndex].add(value);
  }

  @Override
  public void addValue(int columnIndex, double value) {
    result[columnIndex].add(value);
  }

  @Override
  public void addValue(int columnIndex, byte[] value) {
    result[columnIndex].add(value);
  }

  @Override
  public void addValue(int columnIndex, byte value) {
    result[columnIndex].add(value);
  }

  @Override
  public void addValue(int columnIndex, BigDecimal value) {
    result[columnIndex].add(value);
  }

  public void collectColumnList(ColumnDefPacket[] packets) {
    columns = packets;
    result = new ArrayList[packets.length];
    for (int i = 0; i < packets.length; i++) {
      result[i] = new ArrayList();
      columnCount++;
    }
  }


  @Override
  public Iterator<Object[]> iterator() {
    ArrayList[] res = this.result;
    if (res.length == 0) {
      return Collections.emptyIterator();
    }
    int size = result[0].size();
    if (size == 0) {
      return Collections.emptyIterator();
    }
    return new Iterator<Object[]>() {
      ArrayList[] result = res;
      int index;

      @Override
      public boolean hasNext() {

        return index < size;
      }

      @Override
      public Object[] next() {
        Object[] objects = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
          objects[i] = result[i].get(i);
        }
        index++;
        return objects;
      }
    };
  }
}
