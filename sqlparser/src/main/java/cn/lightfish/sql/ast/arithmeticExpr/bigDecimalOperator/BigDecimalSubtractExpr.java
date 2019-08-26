package cn.lightfish.sql.ast.arithmeticExpr.bigDecimalOperator;

import cn.lightfish.sql.ast.RootExecutionContext;
import cn.lightfish.sql.ast.numberExpr.BigDecimalExpr;
import cn.lightfish.sql.ast.valueExpr.ValueExpr;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BigDecimalSubtractExpr implements BigDecimalExpr {

  private final RootExecutionContext context;
  private final ValueExpr left;
  private final ValueExpr right;

  @Override
  public BigDecimal getValue() {
    BigDecimal leftValue = (BigDecimal) this.left.getValue();
    if (leftValue == null) {
      return null;
    }
    BigDecimal rightValue = (BigDecimal) this.right.getValue();
    if (rightValue == null) {
      return null;
    }
    return leftValue.subtract(rightValue);
  }
}