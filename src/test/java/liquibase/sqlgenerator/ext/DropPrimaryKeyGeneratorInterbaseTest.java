package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropPrimaryKeyStatement;
import liquibase.sqlgenerator.ext.DropPrimaryKeyGeneratorInterbase;

public class DropPrimaryKeyGeneratorInterbaseTest extends AbstractSqlGeneratorTest<DropPrimaryKeyStatement> {

    public DropPrimaryKeyGeneratorInterbaseTest() throws Exception {
        this(new DropPrimaryKeyGeneratorInterbase());
    }

    protected DropPrimaryKeyGeneratorInterbaseTest(SqlGenerator<DropPrimaryKeyStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropPrimaryKeyStatement createSampleSqlStatement() {
		DropPrimaryKeyStatement DropPrimaryKeyStatement = new DropPrimaryKeyStatement(null, null, "table_name", "constraint");
		return DropPrimaryKeyStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
       // Object defaultVal = new ColumnConfig().setDefaultValue("null").getDefaultValueObject();
        DropPrimaryKeyStatement statement = new DropPrimaryKeyStatement(null, null, "TABLE_NAME", "CONSTR1");
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("ALTER TABLE TABLE_NAME DROP CONSTRAINT CONSTR1", stmnt);
    }

}
