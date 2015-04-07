package liquibase.sqlgenerator.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropPrimaryKeyGenerator;
import liquibase.statement.core.DropPrimaryKeyStatement;


public class DropPrimaryKeyGeneratorInterbase extends DropPrimaryKeyGenerator {

	private static Logger LOGGER = LoggerFactory.getLogger(DropPrimaryKeyGeneratorInterbase.class);
	
    @Override
    public boolean supports(DropPrimaryKeyStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public Sql[] generateSql(DropPrimaryKeyStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {

        String sql = "ALTER TABLE " +
                database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()) +
                " DROP CONSTRAINT " + statement.getConstraintName();

        LOGGER.debug("DROPPING PRIMARY KEY:");
    	LOGGER.debug(sql);
    	
        return new Sql[] {
                new UnparsedSql(sql, getAffectedPrimaryKey(statement))
        };
    }
}
