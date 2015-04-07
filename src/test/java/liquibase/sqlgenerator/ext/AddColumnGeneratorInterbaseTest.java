package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.AddColumnStatement;
import liquibase.sqlgenerator.ext.AddColumnGeneratorInterbase;

public class AddColumnGeneratorInterbaseTest extends AbstractSqlGeneratorTest<AddColumnStatement> {

    public AddColumnGeneratorInterbaseTest() throws Exception {
        this(new AddColumnGeneratorInterbase());
    }

    protected AddColumnGeneratorInterbaseTest(SqlGenerator<AddColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected AddColumnStatement createSampleSqlStatement() {
		AddColumnStatement addColumnStatement = new AddColumnStatement(null, null, "table_name", "col1", "TIMESTAMP", null);
		return addColumnStatement;
	}
	
	@Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        
        Object defaultVal = new ColumnConfig().setDefaultValue("null").getDefaultValueObject();
        AddColumnStatement statement = new AddColumnStatement(null, null, "TABLE_NAME", "COLUMN1_NAME", "TIMESTAMP", defaultVal);
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        System.out.println(stmnt);
        assertEquals("ALTER TABLE TABLE_NAME ADD COLUMN1_NAME timestamp DEFAULT null", stmnt);
    }
	
	@Test
    public void testWithColumnWithUniqueConstraint() {
        Database database = new InterbaseDatabase();
        
        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME", "COLUMN2_NAME");
        
        Object defaultVal = new ColumnConfig().setDefaultValue("null").getDefaultValueObject();
        AddColumnStatement statement = new AddColumnStatement(null, null, "TABLE_NAME", "COLUMN1_NAME", "TIMESTAMP", defaultVal, uniqueConstraint);
       
        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("ALTER TABLE TABLE_NAME ADD COLUMN1_NAME timestamp DEFAULT null UNIQUE", stmnt);
    }

}
