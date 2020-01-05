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
import axios from 'axios';
import classnames from 'classnames';
// tslint:disable-next-line:no-unused-variable
import { IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/domestic/domestic.reducer';

// tslint:disable-next-line:no-unused-variable

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
      activeTab: '1'
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  callDocument = event => {
    const { customerEntity } = this.props;
    const docName = event.target.id;
    axios({
      url: `api/document?id=${customerEntity.id}&documentName=${docName}`,
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
      link.setAttribute('download', `${customerEntity.lastName}-${customerEntity.firstName}-${docName}-${currentDateToUse}.docx`);
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
    const { customerEntity } = this.props;
    if (customerEntity.type && customerEntity.type !== 'DOMESTIC') {
      return <h1>incorrect type - {customerEntity.type}</h1>;
    } else {
      const toggle = tab => {
        if (this.state.activeTab !== tab) this.setState({ activeTab: tab });
      };
      return (
        <div>
          <h2>
            <b>
              {customerEntity.firstName} {customerEntity.lastName}
            </b>
          </h2>
          <Nav tabs>
            <NavItem>
              <NavLink
                className={classnames({ active: this.state.activeTab === '1' })}
                onClick={() => {
                  toggle('1');
                }}
              >
                Details
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                className={classnames({ active: this.state.activeTab === '2' })}
                onClick={() => {
                  toggle('2');
                }}
              >
                Orders (TBC)
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
              <Link to={`/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
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
                  </Table>
                </div>
                <Row className="justify-content-center">
                  {/*<JhiPagination
                                items={getPaginationItemsNumber(totalOrders, this.state.itemsPerPage)}
                                activePage={this.state.activePage}
                                onSelect={this.handlePagination}
                                maxButtons={5}
                            />*/}
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
            <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
              <DropdownToggle caret color="primary">
                Documents
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem onClick={this.callDocument} id={'dom-record'}>
                  Domestic Record
                </DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
          </Row>
        </div>
      );
    }
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerEntity: customer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail);
