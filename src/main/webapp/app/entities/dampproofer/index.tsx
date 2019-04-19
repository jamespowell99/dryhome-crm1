import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dampproofer from './dampproofer';
import DampprooferDetail from './dampproofer-detail';
import DampprooferUpdate from './dampproofer-update';
import DampprooferDeleteDialog from './dampproofer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DampprooferUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DampprooferUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DampprooferDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dampproofer} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DampprooferDeleteDialog} />
  </>
);

export default Routes;
