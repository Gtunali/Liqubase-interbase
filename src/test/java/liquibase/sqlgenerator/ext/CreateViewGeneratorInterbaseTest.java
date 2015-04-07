package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.CreateViewStatement;
import liquibase.sqlgenerator.ext.CreateViewGeneratorInterbase;

public class CreateViewGeneratorInterbaseTest extends AbstractSqlGeneratorTest<CreateViewStatement> {

    public CreateViewGeneratorInterbaseTest() throws Exception {
        this(new CreateViewGeneratorInterbase());
    }

    protected CreateViewGeneratorInterbaseTest(SqlGenerator<CreateViewStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateViewStatement createSampleSqlStatement() {
		CreateViewStatement CreateViewStatement = new CreateViewStatement(null, null, "VIEW_NAME", "SELECT ID, NAME FROM TABLE", false);
		return CreateViewStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
        CreateViewStatement statement = new CreateViewStatement(null, null, "VIEW_NAME", "SELECT ID, NAME FROM TABLE", false);
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("CREATE VIEW VIEW_NAME AS SELECT ID, NAME FROM TABLE", stmnt);
    }

}
