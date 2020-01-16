import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import {
  Card,
  CardTitle,
  CardText,
  Button,
  Row,
  Col,
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  TabContent,
  TabPane,
  Nav,
  NavItem,
  NavLink,
  Table,
  CardBody,
  CardHeader,
  Container
} from 'reactstrap';
import axios from 'axios';
import classnames from 'classnames';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize, TextFormat, getPaginationItemsNumber, JhiPagination, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/dampproofer/dampproofer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { getCustomerOrders } from '../customer.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getSortState } from 'app/shared/util/dryhome-pagination-utils';

export interface ICustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerDetailState extends IPaginationBaseState {
  dropdownOpen: boolean;
  activeTab: string;
}

export class CustomerDetail extends React.Component<ICustomerDetailProps, ICustomerDetailState> {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.state = {
      dropdownOpen: false,
      activeTab: '1',
      ...getSortState(this.props.location, ITEMS_PER_PAGE)
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
    this.props.getCustomerOrders(this.props.match.params.id);
  }

  sortEntities() {
    const { activePage, itemsPerPage } = this.state;
    this.props.getCustomerOrders(this.props.match.params.id, activePage - 1, itemsPerPage);
    // this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  callDocument = event => {
    const { customerEntity } = this.props;
    const docName = event.target.id;
    axios({
      url: `api/customers/${customerEntity.id}/document?documentName=${docName}`,
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
      link.setAttribute('download', `${customerEntity.companyName}-${docName}-${currentDateToUse}.docx`);
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
    const { customerEntity, customerOrders, totalOrders } = this.props;

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
        {customerEntity.type && customerEntity.type !== 'DAMP_PROOFER' ? (
          <h1>incorrect type - {customerEntity.type}</h1>
        ) : (
          <div>
            <h2>
              <b>{customerEntity.companyName}</b>
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
                              <span id="companyName">Company Name</span>
                            </dt>
                            <dd>{customerEntity.companyName}</dd>

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
                              <span id="title">Name</span>
                            </dt>
                            <dd>
                              {customerEntity.title} {customerEntity.firstName} {customerEntity.lastName}
                            </dd>
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
                    </Col>
                    <Col>
                      <Card>
                        <CardHeader>Other</CardHeader>
                        <CardBody>
                          <dl>
                            <dt>
                              <span id="products">Products</span>
                            </dt>
                            <dd>{customerEntity.products}</dd>
                            <dt>
                              <span id="interested">Interested</span>
                            </dt>
                            <dd>{customerEntity.interested}</dd>
                            <dt>
                              <span id="paid">Paid</span>
                            </dt>
                            <dd>{customerEntity.paid}</dd>
                          </dl>
                        </CardBody>
                      </Card>
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
                                  <TextFormat type="date" value={customerOrder.orderDate} format={APP_LOCAL_DATE_FORMAT} />
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.despatchDate} format={APP_LOCAL_DATE_FORMAT} />
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.invoiceDate} format={APP_LOCAL_DATE_FORMAT} />
                                </td>
                                <td>
                                  <TextFormat type="date" value={customerOrder.paymentDate} format={APP_LOCAL_DATE_FORMAT} />
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
              <Button tag={Link} to="/entity/dampproofer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button tag={Link} to={`/entity/dampproofer/${customerEntity.id}/edit`} replace color="primary">
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
              </Button>
              <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
                <DropdownToggle caret color="primary">
                  Documents
                </DropdownToggle>
                <DropdownMenu>
                  <DropdownItem onClick={this.callDocument} id={'dp-record'}>
                    Damp Proofer Record
                  </DropdownItem>
                  <DropdownItem onClick={this.callDocument} id={'remcon-prod-lit'}>
                    Remcon Prod Lit
                  </DropdownItem>
                  <DropdownItem onClick={this.callDocument} id={'labels'}>
                    Labels
                  </DropdownItem>
                </DropdownMenu>
              </ButtonDropdown>
            </Row>
          </div>
        )}
      </div>
    );
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerEntity: customer.entity,
  customerOrders: customer.customerOrders,
  totalOrders: customer.totalOrders
});

const mapDispatchToProps = { getEntity, getCustomerOrders };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail);
