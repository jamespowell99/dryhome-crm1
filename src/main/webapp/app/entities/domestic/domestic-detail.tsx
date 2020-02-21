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
import classnames from 'classnames';
// tslint:disable-next-line:no-unused-variable
import { TextFormat, getPaginationItemsNumber, JhiPagination, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/domestic/domestic.reducer';
import { APP_LOCAL_DATE_FORMAT, APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION } from 'app/config/constants';
import { getCustomerOrders } from '../customer.reducer';
import { getDocument } from 'app/shared/reducers/doc-generation';
import { getSortState } from 'app/shared/util/dryhome-pagination-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import print from 'print-js';
import moment from 'moment';

// tslint:disable-next-line:no-unused-variable

export interface ICustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerDetailState extends IPaginationBaseState {
  downloadDropdownOpen: boolean;
  printDropdownOpen: boolean;
  activeTab: string;
}

export class CustomerDetail extends React.Component<ICustomerDetailProps, ICustomerDetailState> {
  constructor(props) {
    super(props);

    this.toggleDownload = this.toggleDownload.bind(this);
    this.togglePrint = this.togglePrint.bind(this);
    this.state = {
      downloadDropdownOpen: false,
      printDropdownOpen: false,
      activeTab: '1',
      ...getSortState(this.props.location, ITEMS_PER_PAGE)
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
    this.props.getCustomerOrders(this.props.match.params.id);
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
        `${nextProps.customerEntity.name}-docName-${moment().format(APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION)}.docx`
      );
      document.body.appendChild(link);
      link.click();
    }
  }

  sortEntities() {
    const { activePage, itemsPerPage } = this.state;
    this.props.getCustomerOrders(this.props.match.params.id, activePage - 1, itemsPerPage);
    // this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  callDownload = event => {
    this.props.getDocument('customers', event.target.id, this.props.customerEntity.id, 'DOCX');
  };

  callPrint = event => {
    this.props.getDocument('customers', event.target.id, this.props.customerEntity.id, 'PDF');
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
    const { customerEntity, customerOrders, totalOrders, generatingDocument } = this.props;

    const tab1 = () => {
      toggleTab('1');
    };
    const tab2 = () => {
      toggleTab('2');
    };

    const toggleTab = tab => {
      if (this.state.activeTab !== tab) this.setState({ activeTab: tab });
    };

    return (
      <div>
        {customerEntity.type && customerEntity.type !== 'DOMESTIC' ? (
          <h1>incorrect type - {customerEntity.type}</h1>
        ) : (
          <div>
            <h2>
              <b>
                {customerEntity.firstName} {customerEntity.lastName}
              </b>
            </h2>
            <Nav tabs>
              <NavItem>
                <NavLink className={classnames({ active: this.state.activeTab === '1' })} onClick={tab1}>
                  Details
                </NavLink>
              </NavItem>
              <NavItem>
                <NavLink className={classnames({ active: this.state.activeTab === '2' })} onClick={tab2}>
                  Orders ({totalOrders})
                </NavLink>
              </NavItem>
            </Nav>
            <TabContent activeTab={this.state.activeTab}>
              <TabPane tabId="1">
                <Container>
                  <Row className="mt-3">
                    <Col>
                      <Card>
                        <CardHeader>Company Details</CardHeader>
                        <CardBody>
                          <dl>
                            <dt>
                              <span id="companyId">ID</span>
                            </dt>
                            <dd>{customerEntity.id}</dd>
                            <dt>
                              <span id="title">Name</span>
                            </dt>
                            <dd>
                              {customerEntity.title} {customerEntity.firstName} {customerEntity.lastName}
                            </dd>
                            <dt>
                              <span id="address1">Address</span>
                            </dt>
                            <dd>{customerEntity.address1}</dd>
                            <dd>{customerEntity.address2}</dd>
                            <dd>{customerEntity.address3}</dd>
                            <dd>{customerEntity.town}</dd>
                            <dd>{customerEntity.postCode}</dd>
                          </dl>
                        </CardBody>
                      </Card>
                    </Col>
                    <Col>
                      <Card>
                        <CardHeader>Contact Details</CardHeader>
                        <CardBody>
                          <dl>
                            <dt>
                              <span id="tel">Tel</span>
                            </dt>
                            <dd>{customerEntity.tel}</dd>
                            <dt>
                              <span id="mobile">Mobile</span>
                            </dt>
                            <dd>{customerEntity.mobile}</dd>
                            <dt>
                              <span id="email">Email</span>
                            </dt>
                            <dd>{customerEntity.email}</dd>
                          </dl>
                        </CardBody>
                      </Card>
                      <Card className="mt-3">
                        <CardHeader>Lead</CardHeader>
                        <CardBody>
                          <dl>
                            <dt>
                              <span id="type">Lead</span>
                            </dt>
                            <dd>{customerEntity.lead}</dd>
                            <dt>
                              <span id="type">Lead Name</span>
                            </dt>
                            <dd>{customerEntity.leadName}</dd>
                            <dt>
                              <span id="type">Lead Tel</span>
                            </dt>
                            <dd>{customerEntity.leadTel}</dd>
                            <dt>
                              <span id="type">Lead Mob</span>
                            </dt>
                            <dd>{customerEntity.leadMob}</dd>
                          </dl>
                        </CardBody>
                      </Card>
                    </Col>
                  </Row>
                  <Row className="mt-3">
                    <Col>
                      <Card>
                        <CardBody>
                          <dl>
                            <dt>
                              <span id="type">Status</span>
                            </dt>
                            <dd>{customerEntity.status}</dd>
                          </dl>
                          <Row>
                            <Col>
                              <Card>
                                <CardHeader>Enquiry</CardHeader>
                                <CardBody>
                                  <dl>
                                    <dt>
                                      <span id="type">Property</span>
                                    </dt>
                                    <dd>{customerEntity.enquiryProperty}</dd>
                                    <dt>
                                      <span id="type">Unit P/Q</span>
                                    </dt>
                                    <dd>{customerEntity.enquiryUnitPq}</dd>
                                    <dt>
                                      <span id="type">Inst P/Q</span>
                                    </dt>
                                    <dd>{customerEntity.enquiryInstPq}</dd>
                                  </dl>
                                </CardBody>
                              </Card>
                            </Col>

                            <Col>
                              <Card>
                                <CardHeader>Sale</CardHeader>
                                <CardBody>
                                  <dl>
                                    <dt>
                                      <span id="type">Products</span>
                                    </dt>
                                    <dd>{customerEntity.saleProducts}</dd>
                                    <dt>
                                      <span id="type">Invoice Date</span>
                                    </dt>
                                    <dd>{customerEntity.saleInvoiceDate}</dd>
                                    <dt>
                                      <span id="type">Invoice Number</span>
                                    </dt>
                                    <dd>{customerEntity.saleInvoiceNumber}</dd>
                                    <dt>
                                      <span id="type">Amount</span>
                                    </dt>
                                    <dd>{customerEntity.saleInvoiceAmount}</dd>
                                  </dl>
                                </CardBody>
                              </Card>
                            </Col>
                          </Row>
                        </CardBody>
                      </Card>
                      <Row />
                    </Col>
                  </Row>

                  <Row className="mt-3">
                    <Col>
                      <dl>
                        <dt>
                          {' '}
                          <span id="notes">Notes</span>
                        </dt>
                        <dd>
                          <textarea id="customer-notes" readOnly name="notes" rows={15} cols={100} value={customerEntity.notes} />
                        </dd>
                      </dl>
                    </Col>
                  </Row>
                </Container>
              </TabPane>
              <TabPane tabId="2">
                <Link
                  to={`/entity/customer-order/new?customerId=` + customerEntity.id}
                  className="btn btn-primary float-right jh-create-entity"
                  id="jh-create-entity"
                >
                  <FontAwesomeIcon icon="plus" />
                  &nbsp; Create new Order
                </Link>
                <Container className="mt-1">
                  <div className="table-responsive">
                    <Table responsive className={'mt-1'}>
                      <thead>
                        <tr>
                          <th className="hand">Order Number</th>
                          <th className="hand">Order Date</th>
                          <th className="hand">Despatch Date</th>
                          <th className="hand">Invoice Date</th>
                          <th className="hand">Payment Date</th>
                          <th className="hand">Invoice Number</th>
                          <th className="hand">Total</th>

                          <th />
                        </tr>
                      </thead>
                      <tbody>
                        {customerOrders
                          ? customerOrders.map((customerOrder, i) => (
                              <tr key={`entity-${i}`}>
                                <td>
                                  <Button tag={Link} to={`/entity/customer-order/${customerOrder.id}`} color="link" size="sm">
                                    {customerOrder.orderNumber}
                                  </Button>
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.orderDate} format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                                </td>
                                <td>
                                  <TextFormat
                                    type="date"
                                    value={customerOrder.despatchDate}
                                    format={APP_LOCAL_DATE_FORMAT}
                                    blankOnInvalid
                                  />
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.invoiceDate} format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.paymentDate} format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                                </td>
                                <td>{customerOrder.invoiceNumber}</td>
                                <td>
                                  £
                                  {customerOrder.total
                                    ? customerOrder.total.toLocaleString(undefined, {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2
                                      })
                                    : null}
                                </td>
                              </tr>
                            ))
                          : null}
                      </tbody>
                    </Table>
                  </div>
                  <Row className="justify-content-center">
                    <JhiPagination
                      items={getPaginationItemsNumber(totalOrders, this.state.itemsPerPage)}
                      activePage={this.state.activePage}
                      onSelect={this.handlePagination}
                      maxButtons={5}
                    />
                  </Row>
                </Container>
              </TabPane>
            </TabContent>
            <Row>
              <Button tag={Link} to="/entity/domestic" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button tag={Link} to={`/entity/domestic/${customerEntity.id}/edit`} replace color="primary">
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
              </Button>
              &nbsp;
              <ButtonDropdown isOpen={this.state.downloadDropdownOpen} toggle={this.toggleDownload}>
                <DropdownToggle caret color="primary">
                  Download
                </DropdownToggle>
                <DropdownMenu>
                  <DropdownItem onClick={this.callDownload} id={'dom-record'}>
                    Domestic Record
                  </DropdownItem>
                  <DropdownItem onClick={this.callDownload} id={'labels'}>
                    Labels
                  </DropdownItem>
                </DropdownMenu>
              </ButtonDropdown>
              &nbsp;
              <ButtonDropdown isOpen={this.state.printDropdownOpen} toggle={this.togglePrint}>
                <DropdownToggle caret color="primary">
                  Print
                </DropdownToggle>
                <DropdownMenu>
                  <DropdownItem onClick={this.callPrint} id={'dom-record'}>
                    Domestic Record
                  </DropdownItem>
                  <DropdownItem onClick={this.callPrint} id={'labels'}>
                    Labels
                  </DropdownItem>
                  <DropdownItem onClick={this.callPrint} id={'remcon-domestics'}>
                    Remcon Domestics 345
                  </DropdownItem>
                </DropdownMenu>
              </ButtonDropdown>
            </Row>
            <Row>{generatingDocument ? <span>Generating...</span> : <span />}</Row>
          </div>
        )}
      </div>
    );
  }
}

const mapStateToProps = ({ customer, docGeneration }: IRootState) => ({
  customerEntity: customer.entity,
  customerOrders: customer.customerOrders,
  totalOrders: customer.totalOrders,
  generatingDocument: docGeneration.generatingDocument,
  downloadDocumentBlob: docGeneration.downloadDocumentBlob,
  printDocumentBlob: docGeneration.printDocumentBlob
});

const mapDispatchToProps = { getEntity, getCustomerOrders, getDocument };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail);
