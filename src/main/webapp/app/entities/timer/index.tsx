import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Timer from './timer';
import TimerDetail from './timer-detail';
import TimerUpdate from './timer-update';
import TimerDeleteDialog from './timer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TimerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TimerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TimerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Timer} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TimerDeleteDialog} />
  </>
);

export default Routes;
