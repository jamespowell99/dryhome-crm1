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
import { APP_LOCAL_DATE_FORMAT, APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION } from 'app/config/constants';
import print from 'print-js';
import moment from 'moment';
import { getDocument } from 'app/shared/reducers/doc-generation';

export interface ICustomerOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerOrderDetailState {
  downloadDropdownOpen: boolean;
  printDropdownOpen: boolean;
}

export class CustomerOrderDetail extends React.Component<ICustomerOrderDetailProps, ICustomerOrderDetailState> {
  constructor(props) {
    super(props);

    this.toggleDownload = this.toggleDownload.bind(this);
    this.togglePrint = this.togglePrint.bind(this);
    this.state = {
      downloadDropdownOpen: false,
      printDropdownOpen: false
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.printDocumentBlob && nextProps.printDocumentBlob !== this.props.printDocumentBlob) {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        const b64 = result.replace(/^data:.+;base64,/, '');
        print({ printable: b64, type: 'pdf', base64: true });
      };
      reader.readAsDataURL(new Blob([nextProps.printDocumentBlob]));
    } else if (nextProps.downloadDocumentBlob && nextProps.downloadDocumentBlob !== this.props.downloadDocumentBlob) {
      const url = window.URL.createObjectURL(new Blob([nextProps.downloadDocumentBlob]));
      const link = document.createElement('a');
      link.href = url;
      const currentDate = new Date();
      // todo get docName?
      link.setAttribute(
        'download',
        `${nextProps.customerOrderEntity.customerName}-${nextProps.customerOrderEntity.orderNumber}` +
          `-docName-${moment().format(APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION)}.docx`
      );
      document.body.appendChild(link);
      link.click();
    }
  }

  callDownload = event => {
    this.props.getDocument('customer-orders', event.target.id, this.props.customerOrderEntity.id, 'DOCX');
  };

  callPrint = event => {
    this.props.getDocument('customer-orders', event.target.id, this.props.customerOrderEntity.id, 'PDF');
  };

  toggleDownload() {
    this.setState({
      downloadDropdownOpen: !this.state.downloadDropdownOpen
    });
  }

  togglePrint() {
    this.setState({
      printDropdownOpen: !this.state.printDropdownOpen
    });
  }

  render() {
    const { customerOrderEntity, generatingDocument } = this.props;
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
          <Row>
            <Button tag={Link} to="/entity/customer-order" replace color="info">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/entity/customer-order/${customerOrderEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
            &nbsp;
            <ButtonDropdown isOpen={this.state.downloadDropdownOpen} toggle={this.toggleDownload}>
              <DropdownToggle caret color="primary">
                Download
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem onClick={this.callDownload} id={'customer-invoice'}>
                  Customer Invoice
                </DropdownItem>
                <DropdownItem onClick={this.callDownload} id={'accountant-invoice'}>
                  Accountant Invoice
                </DropdownItem>
                <DropdownItem onClick={this.callDownload} id={'file-invoice'}>
                  File Invoice
                </DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
            &nbsp;
            <ButtonDropdown isOpen={this.state.printDropdownOpen} toggle={this.togglePrint}>
              <DropdownToggle caret color="primary">
                Print
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem onClick={this.callPrint} id={'customer-invoice'}>
                  Customer Invoice
                </DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
          </Row>
          <Row>{generatingDocument ? <span>Generating...</span> : <span />}</Row>
        </Container>
      </div>
    );
  }
}

const mapStateToProps = ({ customerOrder, docGeneration }: IRootState) => ({
  customerOrderEntity: customerOrder.entity,
  generatingDocument: docGeneration.generatingDocument,
  downloadDocumentBlob: docGeneration.downloadDocumentBlob,
  printDocumentBlob: docGeneration.printDocumentBlob
});

const mapDispatchToProps = { getEntity, getDocument };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrderDetail);
