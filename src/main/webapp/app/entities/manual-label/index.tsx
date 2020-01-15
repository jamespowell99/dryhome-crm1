import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ManualLabel from './manual-label';
import ManualLabelDetail from './manual-label-detail';
import ManualLabelUpdate from './manual-label-update';
import ManualLabelDeleteDialog from './manual-label-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ManualLabelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ManualLabelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ManualLabelDetail} />
      <ErrorBoundaryRoute path={match.url} component={ManualLabel} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ManualLabelDeleteDialog} />
  </>
);

export default Routes;
