import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ManualInvoice from './manual-invoice';
import ManualInvoiceDetail from './manual-invoice-detail';
import ManualInvoiceUpdate from './manual-invoice-update';
import ManualInvoiceDeleteDialog from './manual-invoice-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ManualInvoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ManualInvoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ManualInvoiceDetail} />
      <ErrorBoundaryRoute path={match.url} component={ManualInvoice} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ManualInvoiceDeleteDialog} />
  </>
);

export default Routes;
