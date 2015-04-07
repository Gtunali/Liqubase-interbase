package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.ModifyDataTypeStatement;
import liquibase.sqlgenerator.ext.ModifyDataTypeGeneratorInterbase;

public class ModifyDataTypeGeneratorInterbaseTest extends AbstractSqlGeneratorTest<ModifyDataTypeStatement> {

    public ModifyDataTypeGeneratorInterbaseTest() throws Exception {
        this(new ModifyDataTypeGeneratorInterbase());
    }

    protected ModifyDataTypeGeneratorInterbaseTest(SqlGenerator<ModifyDataTypeStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected ModifyDataTypeStatement createSampleSqlStatement() {
		ModifyDataTypeStatement ModifyDataTypeStatement = new ModifyDataTypeStatement(null, null, "table_name", "col1", "Integer");
		return ModifyDataTypeStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
       // Object defaultVal = new ColumnConfig().setDefaultValue("null").getDefaultValueObject();
        ModifyDataTypeStatement statement = new ModifyDataTypeStatement(null, null, "TABLE_NAME", "COLUMN_NAME", "INTEGER");
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("ALTER TABLE TABLE_NAME ALTER COLUMN_NAME TYPE INTEGER", stmnt);
    }

}
