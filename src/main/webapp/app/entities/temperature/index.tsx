import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Temperature from './temperature';
import TemperatureDetail from './temperature-detail';
import TemperatureUpdate from './temperature-update';
import TemperatureDeleteDialog from './temperature-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TemperatureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TemperatureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TemperatureDetail} />
      <ErrorBoundaryRoute path={match.url} component={Temperature} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TemperatureDeleteDialog} />
  </>
);

export default Routes;
