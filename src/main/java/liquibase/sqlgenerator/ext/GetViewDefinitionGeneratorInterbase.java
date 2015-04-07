package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.GetViewDefinitionGenerator;
import liquibase.statement.core.GetViewDefinitionStatement;

public class GetViewDefinitionGeneratorInterbase extends GetViewDefinitionGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(GetViewDefinitionStatement statement, Database database) {
        return database instanceof InterbaseDatabase;
    }

    @Override
    public Sql[] generateSql(GetViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new Sql[] {
                new UnparsedSql("select rdb$view_source from rdb$relations where upper(rdb$relation_name)='" + statement.getViewName() + "'")
        };
    }

}