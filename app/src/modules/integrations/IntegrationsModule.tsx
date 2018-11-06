import { WithRouter } from "@syndesis/app/containers";
import { ModuleLoader } from "@syndesis/ui";
import * as React from "react";
import Loadable from "react-loadable";
import { Route, Switch } from "react-router";

const LoadableIntegrationsPage = Loadable({
  loader: () =>
    import(/* webpackChunkName: "IntegrationsPageChunk" */ "./pages/IntegrationsPage"),
  loading: ModuleLoader
});

export class IntegrationsModule extends React.Component {
  public render() {
    return (
      <WithRouter>
        {({ match }) => (
          <Switch>
            <Route
              path={match.url}
              exact={true}
              component={LoadableIntegrationsPage}
            />
          </Switch>
        )}
      </WithRouter>
    );
  }
}
