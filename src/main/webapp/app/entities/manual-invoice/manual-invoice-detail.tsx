import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import axios from 'axios';

import {
  Button,
  ButtonDropdown,
  Card,
  CardBody,
  CardHeader,
  CardText,
  CardTitle,
  Col,
  Container,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Nav,
  NavItem,
  NavLink,
  Row,
  TabContent,
  Table,
  TabPane
} from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './manual-invoice.reducer';
import { IManualInvoice } from 'app/shared/model/manual-invoice.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IManualInvoiceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IManualInvoiceDetailState {
  dropdownOpen: boolean;
}

export class ManualInvoiceDetail extends React.Component<IManualInvoiceDetailProps, IManualInvoiceDetailState> {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.state = {
      dropdownOpen: false
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  callDocument = event => {
    const { manualInvoiceEntity } = this.props;
    const docName = event.target.id;
    axios({
      url: `api/manual-invoices/${manualInvoiceEntity.id}/document?documentName=${docName}`,
      method: 'GET',
      responseType: 'blob' // important
    }).then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      const currentDate = new Date();
      const currentDateAsString = `${currentDate.getFullYear()}${currentDate.getMonth() + 1}${currentDate.getDate()}`;
      const currentTimeAsString = `${currentDate.getHours()}${currentDate.getMinutes()}${currentDate.getSeconds()}`;
      const currentDateToUse = `${currentDateAsString}${currentTimeAsString}`;
      link.setAttribute('download', `${manualInvoiceEntity.orderNumber}-${docName}-${currentDateToUse}.docx`);
      document.body.appendChild(link);
      link.click();
    });
    // console.log('done');
  };

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  render() {
    const { manualInvoiceEntity } = this.props;
    return (
      <div>
        <Container>
          <Row>
            <Col>
              <h2>
                ManualInvoice [<b>{manualInvoiceEntity.id}</b>]
              </h2>
            </Col>
          </Row>
          <Row>
            <Col>
              <Card>
                <CardHeader>Order Details</CardHeader>
                <CardBody>
                  <dl>
                    <dt>Customer</dt>
                    <dd>{manualInvoiceEntity.customer}</dd>
                    <dt>
                      <span id="invoiceNumber">Invoice Number</span>
                    </dt>
                    <dd>{manualInvoiceEntity.invoiceNumber}</dd>
                    <dt>
                      <span id="orderNumber">Order Number</span>
                    </dt>
                    <dd>{manualInvoiceEntity.orderNumber}</dd>
                    <dt>
                      <span id="invoiceDate">Invoice Date</span>
                    </dt>
                    <dd>
                      <TextFormat value={manualInvoiceEntity.invoiceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                    </dd>
                    <dt>
                      <span id="ref">Ref</span>
                    </dt>
                    <dd>{manualInvoiceEntity.ref}</dd>
                    <dt>
                      <span id="specialInstructions1">Special Instructions 1</span>
                    </dt>
                    <dd>{manualInvoiceEntity.specialInstructions1}</dd>
                    <dt>
                      <span id="specialInstructions2">Special Instructions 2</span>
                    </dt>
                    <dd>{manualInvoiceEntity.specialInstructions2}</dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
            <Col>
              <Card>
                <CardHeader>Invoice Contact / Address</CardHeader>
                <CardBody>
                  <dl>
                    <dt>
                      <span id="address1">Address 1</span>
                    </dt>
                    <dd>{manualInvoiceEntity.address1}</dd>
                    <dt>
                      <span id="address2">Address 2</span>
                    </dt>
                    <dd>{manualInvoiceEntity.address2}</dd>
                    <dt>
                      <span id="address3">Address 3</span>
                    </dt>
                    <dd>{manualInvoiceEntity.address3}</dd>
                    <dt>
                      <span id="town">Town</span>
                    </dt>
                    <dd>{manualInvoiceEntity.town}</dd>
                    <dt>
                      <span id="postCode">Post Code</span>
                    </dt>
                    <dd>{manualInvoiceEntity.postCode}</dd>
                    <dt>
                      <span id="telNo">Tel No</span>
                    </dt>
                    <dd>{manualInvoiceEntity.telNo}</dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
            <Col>
              <Card>
                <CardHeader>Delivery Contact / Address</CardHeader>
                <CardBody>
                  <dl>
                    <dt>
                      <span id="deliveryAddress1">Delivery Address 1</span>
                    </dt>
                    <dd>{manualInvoiceEntity.deliveryAddress1}</dd>
                    <dt>
                      <span id="deliveryAddress2">Delivery Address 2</span>
                    </dt>
                    <dd>{manualInvoiceEntity.deliveryAddress2}</dd>
                    <dt>
                      <span id="deliveryAddress3">Delivery Address 3</span>
                    </dt>
                    <dd>{manualInvoiceEntity.deliveryAddress3}</dd>
                    <dt>
                      <span id="deliveryAddress4">Delivery Address 4</span>
                    </dt>
                    <dd>{manualInvoiceEntity.deliveryAddress4}</dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
          </Row>
          <Row className="mt-3">
            <Col className="mx-2">
              <dt>
                <span id="orderItems">Items</span>
              </dt>
              <div className="table-responsive table-sm">
                <Table responsive className="table-bordered">
                  <thead className="thead-light">
                    <tr>
                      <th className="hand">ID</th>
                      <th className="hand">Product</th>
                      <th className="hand">Quantity</th>
                      <th className="hand">Price</th>
                    </tr>
                  </thead>
                  <tbody>
                    {manualInvoiceEntity.items
                      ? manualInvoiceEntity.items.map((item, i) => (
                          <tr key={`entity-${i}`}>
                            <td>{item.id}</td>
                            <td>{item.product}</td>
                            <td>{item.quantity}</td>
                            <td>
                              £
                              {item.price
                                ? item.price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
                                : null}
                            </td>
                          </tr>
                        ))
                      : null}
                  </tbody>
                </Table>
              </div>
            </Col>
          </Row>
          <Row>
            <Col>
              <Card>
                <CardHeader>Notes</CardHeader>
                <CardBody />
              </Card>
            </Col>
            <Col>
              <Card>
                <CardHeader>Payment Details</CardHeader>
                <CardBody>
                  <dl>
                    <dt>
                      <span id="paymentDate">Payment Date</span>
                    </dt>
                    <dd>
                      <TextFormat value={manualInvoiceEntity.paymentDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                    </dd>
                    <dt>
                      <span id="paymentStatus">Payment Status</span>
                    </dt>
                    <dd>{manualInvoiceEntity.paymentStatus}</dd>
                    <dt>
                      <span id="paymentType">Payment Type</span>
                    </dt>
                    <dd>{manualInvoiceEntity.paymentType}</dd>
                    <dt>
                      <span id="paymentAmount">Payment Amount</span>
                    </dt>
                    <dd>{manualInvoiceEntity.paymentAmount}</dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
            <Col>
              <Card>
                <CardHeader>Totals</CardHeader>
                <CardBody>
                  <dl>
                    <dt>
                      <span id="orderSubTotal">Sub Total</span>
                    </dt>
                    <dd>£{manualInvoiceEntity.subTotal ? manualInvoiceEntity.subTotal.toFixed(2) : null}</dd>
                    <dt>
                      <span id="vatRate">Vat</span>
                    </dt>
                    <dd>
                      £{manualInvoiceEntity.vatAmount ? manualInvoiceEntity.vatAmount.toFixed(2) : null} (
                      {manualInvoiceEntity.vatRate ? manualInvoiceEntity.vatRate.toFixed(1) : null}
                      %)
                    </dd>
                    <dt>
                      <span id="orderTotal">Total</span>
                    </dt>
                    <dd>
                      £
                      {manualInvoiceEntity.total
                        ? manualInvoiceEntity.total.toLocaleString(undefined, {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2
                          })
                        : null}
                    </dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
          </Row>
          <Button tag={Link} to="/entity/manual-invoice" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/manual-invoice/${manualInvoiceEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
          <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
            <DropdownToggle caret color="primary">
              Documents
            </DropdownToggle>
            <DropdownMenu>
              <DropdownItem onClick={this.callDocument} id={'customer-invoice'}>
                Customer Invoice
              </DropdownItem>
              <DropdownItem onClick={this.callDocument} id={'accountant-invoice'}>
                Accountant Invoice
              </DropdownItem>
              <DropdownItem onClick={this.callDocument} id={'file-invoice'}>
                File Invoice
              </DropdownItem>
            </DropdownMenu>
          </ButtonDropdown>
        </Container>
      </div>
    );
  }
}

const mapStateToProps = ({ manualInvoice }: IRootState) => ({
  manualInvoiceEntity: manualInvoice.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManualInvoiceDetail);
