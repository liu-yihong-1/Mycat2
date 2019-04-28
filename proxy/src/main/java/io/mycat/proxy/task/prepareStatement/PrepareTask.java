package io.mycat.proxy.task.prepareStatement;

import io.mycat.beans.mysql.MySQLPStmtBindValueList;
import io.mycat.proxy.packet.ColumnDefPacket;
import io.mycat.proxy.packet.ColumnDefPacketImpl;
import io.mycat.proxy.packet.MySQLPacket;
import io.mycat.proxy.packet.PreparedOKPacket;
import io.mycat.proxy.session.MySQLSession;
import io.mycat.proxy.task.AsynTaskCallBack;
import io.mycat.proxy.task.ResultSetTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrepareTask implements ResultSetTask, PreparedStatement {
    long statementId;
    String prepareStatement;
    int[] parameterTypeList;
    ColumnDefPacket[] columnDefList;
    boolean newParameterBoundFlag = false;
    MySQLPStmtBindValueList valueList;
    Map<Integer,ByteArrayOutputStream> longDataMap;

    public void request(MySQLSession mysql, String prepareStatement, AsynTaskCallBack<MySQLSession> callBack) {
        this.prepareStatement = prepareStatement;
        request(mysql, 0x16, prepareStatement, callBack);
        mysql.prepareReveicePrepareOkResponse();
    }

    @Override
    public void onPrepareOk(PreparedOKPacket packet) {
        statementId = packet.getPreparedOkStatementId();
        int warningCount = packet.getPreparedOkWarningCount();

        columnDefList = new ColumnDefPacket[packet.getPrepareOkColumnsCount()];
        parameterTypeList = new int[packet.getPrepareOkParametersCount()];
        longDataMap = new HashMap<>();
    }

    @Override
    public void onFinished(boolean success, String errorMessage) {
        valueList = new MySQLPStmtBindValueList(this);
        MySQLSession currentMySQLSession = getCurrentMySQLSession();
        AsynTaskCallBack<MySQLSession> callBack = currentMySQLSession.getCallBackAndReset();
        callBack.finished(currentMySQLSession, this, success, this, errorMessage);
    }

    @Override
    public void onPrepareOkParameterDef(MySQLPacket buffer, int startPos, int endPos) {
        ColumnDefPacketImpl columnDefPacket = new ColumnDefPacketImpl();
        columnDefPacket.read(buffer, startPos, endPos);
        int prepareOkParametersCount = getCurrentMySQLSession().getPacketResolver().getPrepareOkParametersCount();
        int length = parameterTypeList.length-1;
        int index = length - prepareOkParametersCount;
        parameterTypeList[index] = (byte) columnDefPacket.getColumnType();
    }

    @Override
    public void onPrepareOkColumnDef(MySQLPacket buffer, int startPos, int endPos) {
        ColumnDefPacketImpl columnDefPacket = new ColumnDefPacketImpl();
        columnDefPacket.read(buffer, startPos, endPos);
        int prepareOkColumnsCount = getCurrentMySQLSession().getPacketResolver().getPrepareOkColumnsCount();
        columnDefList[columnDefList.length - prepareOkColumnsCount-1] = columnDefPacket;
    }


    @Override
    public long getStatementId() {
        return statementId;
    }

    @Override
    public int getColumnsNumber() {
        if (columnDefList == null) return 0;
        return columnDefList.length;
    }

    @Override
    public int getParametersNumber() {
        if (parameterTypeList == null) return 0;
        return parameterTypeList.length;
    }

    @Override
    public  Map<Integer,ByteArrayOutputStream> getLongDataMap() {
        return longDataMap;
    }


    @Override
    public boolean setNewParameterBoundFlag(boolean b) {
        return this.newParameterBoundFlag = b;
    }

    @Override
    public int[] getParameterTypeList() {
        return parameterTypeList;
    }

    @Override
    public ColumnDefPacket[] getColumnDefList() {
        return columnDefList;
    }

    @Override
    public MySQLPStmtBindValueList getBindValueList() {
        return valueList;
    }

    @Override
    public boolean isNewParameterBoundFlag() {
        return newParameterBoundFlag;
    }
}
