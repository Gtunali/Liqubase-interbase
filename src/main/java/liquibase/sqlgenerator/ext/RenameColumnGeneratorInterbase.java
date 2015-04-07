package liquibase.sqlgenerator.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.RenameColumnGenerator;
import liquibase.statement.core.RenameColumnStatement;

public class RenameColumnGeneratorInterbase extends RenameColumnGenerator {
	
	Logger LOGGER = LoggerFactory.getLogger(RenameColumnGeneratorInterbase.class);

    @Override
    public boolean supports(RenameColumnStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public ValidationErrors validate(RenameColumnStatement renameColumnStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("tableName", renameColumnStatement.getTableName());
        validationErrors.checkRequiredField("oldColumnName", renameColumnStatement.getOldColumnName());
        validationErrors.checkRequiredField("newColumnName", renameColumnStatement.getNewColumnName());

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(RenameColumnStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql;
        
        sql = "ALTER TABLE " + database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()) +
                " ALTER COLUMN " + database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getOldColumnName()) +
                " TO " + database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getNewColumnName());

        LOGGER.debug("RENAME COLUMN:");
    	LOGGER.debug(sql);
    	
        return new Sql[] {
                new UnparsedSql(sql, getAffectedOldColumn(statement), getAffectedNewColumn(statement))
        };
    }
}
