package liquibase.sqlgenerator.ext;

import liquibase.change.ConstraintsConfig;
import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.LiquibaseDataType;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.CreateTableStatement;

import org.junit.Test;

import static org.junit.Assert.*;


public class CreateTableGeneratorInterbaseTest extends AbstractSqlGeneratorTest<CreateTableStatement> {

    public CreateTableGeneratorInterbaseTest() throws Exception {
        this(new CreateTableGeneratorInterbase());
    }

    protected CreateTableGeneratorInterbaseTest(SqlGenerator<CreateTableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateTableStatement createSampleSqlStatement() {
        Database database = new InterbaseDatabase();
        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("int(11) unsigned", database), database);
        return createTableStatement;
    }


    @Test
    public void testWithColumnWithDefaultValue() {
        Database database = new InterbaseDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        LiquibaseDataType type = DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP", database);
        statement.addColumn("COLUMN1_NAME", type,
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("null").getDefaultValueObject());

        String stmnt = this.generatorUnderTest.generateSql(statement, database, null)[0].toSql();
        assertEquals("CREATE TABLE TABLE_NAME (COLUMN1_NAME timestamp DEFAULT null)", stmnt);
    }


    @Test
    public void testWithColumnSpecificIntType() {
        Database database = new InterbaseDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int", database));

        assertEquals("CREATE TABLE TABLE_NAME (COLUMN1_NAME INTEGER)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


    @Test
    public void testCreateMultiColumnPrimary() throws Exception {
        Database database = new InterbaseDatabase();

        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)", database),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("author",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)", database),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("dateExecuted",
                DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP", database),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("description",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(255)", database));
        createTableStatement.addColumn("revision",
                DataTypeFactory.getInstance().fromDescription("int", database));

        Database interbasedb = new InterbaseDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createTableStatement, interbasedb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createTableStatement, interbasedb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE TABLE table_name (id VARCHAR(150) DEFAULT null, author VARCHAR(150) DEFAULT null, " +
                "dateExecuted timestamp DEFAULT null, description VARCHAR(255), revision INTEGER)",
                generatedSql[0].toSql());
    }


    @Test
    public void testWithColumnUniqueConstraint() {
        Database database = new InterbaseDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int", database),
                new ColumnConfig().setConstraints(new ConstraintsConfig().setNullable(false)).getDefaultValueObject());

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE TABLE_NAME (COLUMN1_NAME INTEGER, UNIQUE (COLUMN1_NAME))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testWith2ColumnsUniqueConstraint() {
        Database database = new InterbaseDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME", "COLUMN2_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int", database));
        statement.addColumn("COLUMN2_NAME",
                DataTypeFactory.getInstance().fromDescription("varchar(100)", database));

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE TABLE_NAME (COLUMN1_NAME INTEGER, COLUMN2_NAME VARCHAR(100), UNIQUE (COLUMN1_NAME, COLUMN2_NAME))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }
    

}
