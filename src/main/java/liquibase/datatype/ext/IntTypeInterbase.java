package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.IntType;

@DataTypeInfo(name = "int", aliases = {"integer", "java.sql.Types.INTEGER", "java.lang.Integer", "serial"},
        minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class IntTypeInterbase extends IntType {

    private boolean autoIncrement;

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }


    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof InterbaseDatabase) {
            return new DatabaseDataType("INTEGER");
        }
        return super.toDatabaseDataType(database);
    }

}