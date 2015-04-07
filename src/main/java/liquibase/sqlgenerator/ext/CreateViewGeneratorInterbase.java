package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.CreateViewGenerator;
import liquibase.statement.core.CreateViewStatement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateViewGeneratorInterbase extends CreateViewGenerator {
	
	Logger LOGGER = LoggerFactory.getLogger(CreateViewGeneratorInterbase.class);

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(CreateViewStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public ValidationErrors validate(CreateViewStatement createViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors.checkRequiredField("viewName", createViewStatement.getViewName());
        validationErrors.checkRequiredField("selectQuery", createViewStatement.getSelectQuery());

        if (createViewStatement.isReplaceIfExists()) {
            validationErrors.checkDisallowedField("replaceIfExists", createViewStatement.isReplaceIfExists(), database, InterbaseDatabase.class);
        }

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(CreateViewStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
      
        List<Sql> sql = new ArrayList<Sql>();
        String query = "CREATE VIEW " + database.escapeViewName(statement.getCatalogName(), statement.getSchemaName(), statement.getViewName()) +
                " AS " + statement.getSelectQuery();

        sql.add(new UnparsedSql(query , getAffectedView(statement)) );

        LOGGER.debug("CREATING VIEW: ");
        LOGGER.debug(query);
        
        return sql.toArray(new Sql[sql.size()]);
    }

}
