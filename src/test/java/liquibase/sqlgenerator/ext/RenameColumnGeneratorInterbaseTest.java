package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.RenameColumnStatement;
import liquibase.sqlgenerator.ext.RenameColumnGeneratorInterbase;

public class RenameColumnGeneratorInterbaseTest extends AbstractSqlGeneratorTest<RenameColumnStatement> {

    public RenameColumnGeneratorInterbaseTest() throws Exception {
        this(new RenameColumnGeneratorInterbase());
    }

    protected RenameColumnGeneratorInterbaseTest(SqlGenerator<RenameColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected RenameColumnStatement createSampleSqlStatement() {
		RenameColumnStatement RenameColumnStatement = new RenameColumnStatement(null, null, "table_name", "col1", "col2", "type");
		return RenameColumnStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
        RenameColumnStatement statement = new RenameColumnStatement(null, null, "TABLE_NAME", "OLD_COLUMN_NAME", "NEW_COLUMN_NAME", "VARCHAR(10)");
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("ALTER TABLE TABLE_NAME ALTER COLUMN OLD_COLUMN_NAME TO NEW_COLUMN_NAME", stmnt);
    }

}
