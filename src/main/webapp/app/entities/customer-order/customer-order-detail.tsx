import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-order.reducer';
import { ICustomerOrder } from 'app/shared/model/customer-order.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CustomerOrderDetail extends React.Component<ICustomerOrderDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customerOrderEntity } = this.props;
    return (
      <div>
        <Row>
          <Col>
            <h2>
              Order{' '}
              <b>
                <span id="orderNumber">{customerOrderEntity.orderNumber}</span>
              </b>
            </h2>
          </Col>
        </Row>
        <Row>
          <Col>
            <dl>
              <dt>Customer</dt>
              <dd>
                {customerOrderEntity.customerName}({customerOrderEntity.customerId})
              </dd>

              <dt>
                <span id="orderDate">Order Date</span>
              </dt>
              <dd>
                <TextFormat value={customerOrderEntity.orderDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
              </dd>
              <dt>
                <span id="despatchDate">Despatch Date</span>
              </dt>
              <dd>
                <TextFormat value={customerOrderEntity.despatchDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
              </dd>
              <dt>
                <span id="invoiceDate">Invoice Date</span>
              </dt>
              <dd>
                <TextFormat value={customerOrderEntity.invoiceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
              </dd>
              <dt>
                <span id="placedBy">Placed By</span>
              </dt>
              <dd>{customerOrderEntity.placedBy}</dd>
              <dt>
                <span id="method">Method</span>
              </dt>
              <dd>{customerOrderEntity.method}</dd>
              <dt>
                <span id="invoiceNumber">Invoice Number</span>
              </dt>
              <dd>{customerOrderEntity.invoiceNumber}</dd>
              <dt>
                <span id="notes1">Notes 1</span>
              </dt>
              <dd>{customerOrderEntity.notes1}</dd>
              <dt>
                <span id="notes2">Notes 2</span>
              </dt>
              <dd>{customerOrderEntity.notes2}</dd>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="method">Invoice Contact / Address</span>
              </dt>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="method">Invoice Contact / Address</span>
              </dt>
            </dl>
          </Col>
        </Row>
        <Row>
          <div className="table-responsive table-sm">
            <Table responsive className="table-bordered">
              <thead className="thead-light">
                <tr>
                  <th className="hand">ID</th>
                  <th className="hand">Product</th>
                  <th className="hand">Quantity</th>
                  <th className="hand">Price</th>
                  <th className="hand">notes</th>
                  <th className="hand">serial number</th>
                </tr>
              </thead>
              <tbody>
                {customerOrderEntity.items
                  ? customerOrderEntity.items.map((item, i) => (
                      <tr key={`entity-${i}`}>
                        <td>{item.id}</td>
                        <td>{item.product}</td>
                        <td>{item.quantity}</td>
                        <td>£{item.price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
                        <td>{item.notes}</td>
                        <td>{item.serialNumber}</td>
                      </tr>
                    ))
                  : null}
              </tbody>
            </Table>
          </div>
        </Row>
        <Row>
          <Col>
            <dl>
              <dt>
                <span id="internalNotes">Internal Notes</span>
              </dt>
              <dd>{customerOrderEntity.internalNotes}</dd>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="paymentDate">Payment Date</span>
              </dt>
              <dd>
                <TextFormat value={customerOrderEntity.paymentDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
              </dd>
              <dt>
                <span id="paymentStatus">Payment Status</span>
              </dt>
              <dd>{customerOrderEntity.paymentStatus}</dd>
              <dt>
                <span id="paymentType">Payment Type</span>
              </dt>
              <dd>{customerOrderEntity.paymentType}</dd>
              <dt>
                <span id="paymentAmount">Payment Amount</span>
              </dt>
              <dd>{customerOrderEntity.paymentAmount}</dd>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="vatRate">Vat Rate</span>
              </dt>
              <dd>{customerOrderEntity.vatRate ? customerOrderEntity.vatRate.toFixed(2) : null}%</dd>
              <dt>
                <span id="vatRate">Total</span>
              </dt>
              <dd>
                £
                {customerOrderEntity.orderTotal
                  ? customerOrderEntity.orderTotal.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
                  : null}
              </dd>
            </dl>
          </Col>
        </Row>
        <Button tag={Link} to="/entity/customer-order" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entity/customer-order/${customerOrderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </div>
    );
  }
}

const mapStateToProps = ({ customerOrder }: IRootState) => ({
  customerOrderEntity: customerOrder.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrderDetail);
