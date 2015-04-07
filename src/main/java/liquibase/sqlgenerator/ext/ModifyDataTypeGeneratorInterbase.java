package liquibase.sqlgenerator.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.datatype.DataTypeFactory;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.ModifyDataTypeGenerator;
import liquibase.statement.core.ModifyDataTypeStatement;
import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;

public class ModifyDataTypeGeneratorInterbase extends ModifyDataTypeGenerator {

	Logger LOGGER = LoggerFactory.getLogger(ModifyDataTypeGeneratorInterbase.class);

    @Override
    public boolean supports(ModifyDataTypeStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public Sql[] generateSql(ModifyDataTypeStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String alterTable = "ALTER TABLE " + database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName());

        // add "MODIFY"
        alterTable += " " + getModifyString(database) + " ";

        // add column name
        alterTable += database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getColumnName());

        alterTable += getPreDataTypeString(database); // adds a space if nothing else

        // add column type
        alterTable += DataTypeFactory.getInstance().fromDescription(statement.getNewDataType(), database).toDatabaseDataType(database);

        return new Sql[]{new UnparsedSql(alterTable, getAffectedTable(statement))};
    }

    /**
     * @return either "MODIFY" or "ALTER COLUMN" depending on the current db
     */
    public String getModifyString(Database database) 
    {
            return "ALTER";
    }

    /**
     * @return the string that comes before the column type
     *         definition (like 'set data type' for derby or an open parentheses for Oracle)
     */
    public String getPreDataTypeString(Database database)
    {
            return " TYPE ";
    }
}
