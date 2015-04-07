package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.sqlgenerator.core.AddColumnGenerator;
import liquibase.statement.AutoIncrementConstraint;
import liquibase.statement.core.AddColumnStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
;

public class AddColumnGeneratorInterbase extends AddColumnGenerator {

	Logger LOGGER = LoggerFactory.getLogger(AddColumnGeneratorInterbase.class);
	
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddColumnStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    protected String generateSingleColumnSQL(AddColumnStatement statement, Database database) {

        String alterTable = null;

        alterTable =  " ADD " + database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) +
                " " + DataTypeFactory.getInstance().fromDescription(statement.getColumnType() + (statement.isAutoIncrement() ? "{autoIncrement:true}" : ""), database).toDatabaseDataType(database);

        if (statement.isAutoIncrement() && database.supportsAutoIncrement()) {
            AutoIncrementConstraint autoIncrementConstraint = statement.getAutoIncrementConstraint();
            alterTable += " " + database.getAutoIncrementClause(autoIncrementConstraint.getStartWith(), autoIncrementConstraint.getIncrementBy());
        }
        

        alterTable += getDefaultClause(statement, database);
        
        if (!statement.isNullable()) {
            alterTable += " NOT NULL";
        }

        if (statement.isPrimaryKey()) {
            alterTable += " PRIMARY KEY";
        }

        // "ALTER TABLE ADD COLUMN ...UNIQUE " 
        if (statement.isUnique()) {
            alterTable += " UNIQUE";
        }

        LOGGER.debug("ADDING COLUMN:");
    	LOGGER.debug(alterTable);
    	
    	return alterTable;
    }

    private String getDefaultClause(AddColumnStatement statement, Database database) {
        String clause = "";
        Object defaultValue = statement.getDefaultValue();
        if (defaultValue != null) {
            clause += " DEFAULT " + DataTypeFactory.getInstance().fromObject(defaultValue, database).objectToSql(defaultValue, database);
        }
        return clause;
    }

}