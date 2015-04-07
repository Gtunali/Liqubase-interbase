package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropColumnStatement;
import liquibase.sqlgenerator.ext.DropColumnGeneratorInterbase;

public class DropColumnGeneratorInterbaseTest extends AbstractSqlGeneratorTest<DropColumnStatement> {

    public DropColumnGeneratorInterbaseTest() throws Exception {
        this(new DropColumnGeneratorInterbase());
    }

    protected DropColumnGeneratorInterbaseTest(SqlGenerator<DropColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropColumnStatement createSampleSqlStatement() {
		DropColumnStatement DropColumnStatement = new DropColumnStatement(null, null, "table_name", "col1");
		return DropColumnStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
       // Object defaultVal = new ColumnConfig().setDefaultValue("null").getDefaultValueObject();
        DropColumnStatement statement = new DropColumnStatement(null, null, "TABLE_NAME", "COLUMN1_NAME");
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("ALTER TABLE TABLE_NAME DROP COLUMN1_NAME", stmnt);
    }

}
