import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
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
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-order.reducer';
// tslint:disable-next-line:no-unused-variable
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import axios from 'axios';
import print from 'print-js';

export interface ICustomerOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerOrderDetailState {
  dropdownOpen: boolean;
}

export class CustomerOrderDetail extends React.Component<ICustomerOrderDetailProps, ICustomerOrderDetailState> {
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
    const { customerOrderEntity } = this.props;
    const docName = event.target.id;

    axios({
      url: `api/customer-orders/${customerOrderEntity.id}/document?documentName=${docName}`,
      method: 'GET',
      responseType: 'blob' // important
    }).then(response => {
      if (docName.endsWith('-pdf')) {
        // todo is there a nicer way to convert to base
        const reader = new FileReader();
        reader.onload = () => {
          // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
          const result = reader.result as string;
          const b64 = result.replace(/^data:.+;base64,/, '');
          print({ printable: b64, type: 'pdf', base64: true });
        };

        reader.readAsDataURL(new Blob([response.data]));
      } else {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        const currentDate = new Date();
        const currentDateAsString = `${currentDate.getFullYear()}${currentDate.getMonth() + 1}${currentDate.getDate()}`;
        const currentTimeAsString = `${currentDate.getHours()}${currentDate.getMinutes()}${currentDate.getSeconds()}`;
        const currentDateToUse = `${currentDateAsString}${currentTimeAsString}`;
        link.setAttribute(
          'download',
          `${customerOrderEntity.customerName}-${customerOrderEntity.orderNumber}-${docName}-${currentDateToUse}.docx`
        );
        document.body.appendChild(link);
        link.click();
      }
    });
  };

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  render() {
    const { customerOrderEntity } = this.props;
    return (
      <div>
        <Container>
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
              <Card>
                <CardHeader>Order Details</CardHeader>
                <CardBody>
                  <dl>
                    <dt>Customer</dt>
                    <dd>
                      {customerOrderEntity.customerId ? (
                        <Link
                          to={
                            customerOrderEntity.customerType === 'DOMESTIC'
                              ? `/entity/domestic/${customerOrderEntity.customerId}`
                              : `/entity/dampproofer/${customerOrderEntity.customerId}`
                          }
                        >
                          {customerOrderEntity.customerName}({customerOrderEntity.customerId})
                        </Link>
                      ) : (
                        ''
                      )}
                    </dd>

                    <dt>
                      <span id="orderDate">Order Date</span>
                    </dt>
                    <dd>
                      <TextFormat value={customerOrderEntity.orderDate} type="date" format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                    </dd>
                    <dt>
                      <span id="despatchDate">Despatch Date</span>
                    </dt>
                    <dd>
                      <TextFormat value={customerOrderEntity.despatchDate} type="date" format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                    </dd>
                    <dt>
                      <span id="invoiceDate">Invoice Date</span>
                    </dt>
                    <dd>
                      <TextFormat value={customerOrderEntity.invoiceDate} type="date" format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
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
                      <span id="notes1">Invoice Notes 1</span>
                    </dt>
                    <dd>{customerOrderEntity.notes1}</dd>
                    <dt>
                      <span id="notes2">Invoice Notes 2</span>
                    </dt>
                    <dd>{customerOrderEntity.notes2}</dd>
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
                      <span id="invoiceContact">Contact</span>
                    </dt>
                    <dd>{customerOrderEntity.invoiceContact}</dd>

                    <dt>
                      <span id="invoiceAddress">Address</span>
                    </dt>
                    <dd>{customerOrderEntity.invoiceAddress ? customerOrderEntity.invoiceAddress.address1 : null}</dd>
                    <dd>{customerOrderEntity.invoiceAddress ? customerOrderEntity.invoiceAddress.address2 : null}</dd>
                    <dd>{customerOrderEntity.invoiceAddress ? customerOrderEntity.invoiceAddress.address3 : null}</dd>
                    <dd>{customerOrderEntity.invoiceAddress ? customerOrderEntity.invoiceAddress.town : null}</dd>
                    <dd>{customerOrderEntity.invoiceAddress ? customerOrderEntity.invoiceAddress.postCode : null}</dd>
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
                      <span id="deliveryContact">Contact</span>
                    </dt>
                    <dd>{customerOrderEntity.deliveryContact}</dd>

                    <dt>
                      <span id="deliveryAddress">Address</span>
                    </dt>
                    <dd>{customerOrderEntity.deliveryAddress ? customerOrderEntity.deliveryAddress.address1 : null}</dd>
                    <dd>{customerOrderEntity.deliveryAddress ? customerOrderEntity.deliveryAddress.address2 : null}</dd>
                    <dd>{customerOrderEntity.deliveryAddress ? customerOrderEntity.deliveryAddress.address3 : null}</dd>
                    <dd>{customerOrderEntity.deliveryAddress ? customerOrderEntity.deliveryAddress.town : null}</dd>
                    <dd>{customerOrderEntity.deliveryAddress ? customerOrderEntity.deliveryAddress.postCode : null}</dd>
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
                            <td>{item.product}</td>
                            <td>{item.quantity}</td>
                            <td>
                              £
                              {item.price
                                ? item.price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
                                : null}
                            </td>
                            <td>{item.notes}</td>
                            <td>{item.serialNumber}</td>
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
                <CardBody>
                  <dl>
                    <dd>{customerOrderEntity.internalNotes}</dd>
                  </dl>
                </CardBody>
              </Card>
            </Col>
            <Col>
              <Card>
                <CardHeader>Payment Details</CardHeader>
                <CardBody>
                  {customerOrderEntity.paymentDate ? (
                    <dl>
                      <dt>
                        <span id="paymentDate">Payment Date</span>
                      </dt>
                      <dd>
                        <TextFormat value={customerOrderEntity.paymentDate} type="date" format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
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
                  ) : (
                    <div />
                  )}
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
                    <dd>£{customerOrderEntity.subTotal ? customerOrderEntity.subTotal.toFixed(2) : null}</dd>
                    <dt>
                      <span id="vatRate">Vat</span>
                    </dt>
                    <dd>
                      £{customerOrderEntity.vatAmount ? customerOrderEntity.vatAmount.toFixed(2) : null} (
                      {customerOrderEntity.vatRate ? customerOrderEntity.vatRate.toFixed(1) : null}
                      %)
                    </dd>
                    <dt>
                      <span id="orderTotal">Total</span>
                    </dt>
                    <dd>
                      £
                      {customerOrderEntity.total
                        ? customerOrderEntity.total.toLocaleString(undefined, {
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
          <Button tag={Link} to="/entity/customer-order" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/customer-order/${customerOrderEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
          <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
            <DropdownToggle caret color="primary">
              Documents
            </DropdownToggle>
            <DropdownMenu>
              <DropdownItem onClick={this.callDocument} id={'customer-invoice-pdf'}>
                Customer Invoice (PDF)
              </DropdownItem>
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
