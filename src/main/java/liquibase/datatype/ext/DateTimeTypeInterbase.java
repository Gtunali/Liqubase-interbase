package liquibase.datatype.ext;

import liquibase.database.Database;
//import liquibase.database.core.*;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.DateTimeType;
//import liquibase.statement.DatabaseFunction;

@DataTypeInfo(name = "datetime", aliases = {"java.sql.Types.DATETIME", "java.util.Date"},
        minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class DateTimeTypeInterbase extends DateTimeType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof InterbaseDatabase) {
            return new DatabaseDataType("TIMESTAMP");
        }

        return super.toDatabaseDataType(database);
    }

}