import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Domestic from './domestic';
import DomesticDetail from './domestic-detail';
import DomesticUpdate from './domestic-update';
import DomesticDeleteDialog from './domestic-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DomesticUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DomesticUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DomesticDetail} />
      <ErrorBoundaryRoute path={match.url} component={Domestic} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DomesticDeleteDialog} />
  </>
);

export default Routes;
