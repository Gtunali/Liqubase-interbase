package liquibase.sqlgenerator.ext;

import java.util.ArrayList;
import java.util.List;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddAutoIncrementGenerator;
import liquibase.statement.core.AddAutoIncrementStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddAutoIncrementGeneratorInterbase extends AddAutoIncrementGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddAutoIncrementGeneratorInterbase.class);

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddAutoIncrementStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public ValidationErrors validate(
            AddAutoIncrementStatement statement,
            Database database,
            SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors.checkRequiredField("columnName", statement.getColumnName());
        validationErrors.checkRequiredField("tableName", statement.getTableName());

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(
    		AddAutoIncrementStatement statement,
    		Database database,
    		SqlGeneratorChain sqlGeneratorChain) {
    	
    	List<Sql> returnSql = new ArrayList<Sql>();
    	
    	// create a generator name using the table name
    	String genName = statement.getTableName() + "_GEN";
    	String cmnd1 = ("CREATE GENERATOR " + genName + ";");
    	returnSql.add(new UnparsedSql(cmnd1, getAffectedColumn(statement)));
    	LOGGER.debug("Executing: " + cmnd1);
    	
    	String cmnd2 ="SET GENERATOR " + genName + " TO "+ statement.getStartWith() +";";
    	returnSql.add(new UnparsedSql(cmnd2, getAffectedColumn(statement)));
    	LOGGER.debug("Executing: " + cmnd2);
    	
    	String cmnd3 = "CREATE TRIGGER " + statement.getTableName() + "_ADD FOR " +  statement.getTableName() + " " +
				   "BEFORE INSERT POSITION " + statement.getStartWith() + " AS " +
				   "BEGIN NEW." + statement.getColumnName() + " = GEN_ID(" + genName + ", " + statement.getIncrementBy() + "); " +
				   "END;";
    	
    	/*String cmnd3 = "Create PROCEDURE GET_NEXT_" + statement.getTableName() + "_SEQNUM " +
    		"RETURNS ( NEXTNUM INTEGER) AS " +
			"BEGIN " +
			"nextNum = GEN_ID(" + genName + ", 1); " +
			"WHILE (EXISTS (SELECT DISTINCT " + statement.getColumnName() + " FROM " + statement.getTableName() + " WHERE " + statement.getColumnName() + " = :nextNum)) DO "+
			"BEGIN "+
			"nextNum = GEN_ID(" + genName + ", 1); "+
			"END "+
			"EXIT; "+
			"END  ";*/
    	
    	LOGGER.debug("Executing: " + cmnd3);
    	LOGGER.debug("GENERATING AUTO INCREMENT:");
    	LOGGER.debug(cmnd3);
    	
    	returnSql.add(new UnparsedSql(cmnd3, getAffectedColumn(statement)));

        return returnSql.toArray(new Sql[returnSql.size()]);
    }
}