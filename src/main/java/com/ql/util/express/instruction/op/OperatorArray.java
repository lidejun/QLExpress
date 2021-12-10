package com.ql.util.express.instruction.op;

import java.util.List;

import com.ql.util.express.ArraySwap;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.OperateDataCacheManager;

public class OperatorArray extends OperatorBase {
    public OperatorArray(String aName) {
        this.name = aName;
    }

    public OperatorArray(String aAliasName, String aName, String aErrorInfo) {
        this.name = aName;
        this.aliasName = aAliasName;
        this.errorInfo = aErrorInfo;
    }

    public OperateData executeInner(InstructionSetContext context, ArraySwap list) throws Exception {
        OperateData p0 = list.get(0);
        if (p0 == null || p0.getObject(context) == null) {
            throw new QLException("对象为null,不能执行数组相关操作");
        }
        Object tmpObject = p0.getObject(context);

        if (!tmpObject.getClass().isArray()) {
            Object property = list.get(1).getObject(context);
            //支持data.get(index) ->data[index]
            if (tmpObject instanceof List && property instanceof Number) {
                int index = ((Number)property).intValue();
                return OperateDataCacheManager.fetchOperateDataArrayItem(p0, index);
            }
            //支持data.code -> data['code']
            if (property instanceof String || property instanceof Character) {
                return OperateDataCacheManager.fetchOperateDataField(tmpObject, String.valueOf(property));
            }
        }
        //支持原生Array：data[index]
        int index = ((Number)list.get(1).getObject(context)).intValue();
        return OperateDataCacheManager.fetchOperateDataArrayItem(p0, index);
    }
}
