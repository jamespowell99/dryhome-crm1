import React from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Container, InputGroup, Row, Table, Collapse, Card, CardBody } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { getPaginationItemsNumber, IPaginationBaseState, JhiPagination, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getSortState } from 'app/shared/util/dryhome-pagination-utils';
import {
  getEntities,
  getSearchEntities,
  searchFromOrderDateChanged,
  searchInvoiceNumberChanged,
  searchToOrderDateChanged
} from './customer-order.reducer';
// tslint:disable-next-line:no-unused-variable
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import moment from 'moment';

export interface ICustomerOrderProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICustomerOrderState extends IPaginationBaseState {
  searchStatus: string;
  searchOrderNumber: string;
  searchTabOpen: boolean;
}

export class CustomerOrder extends React.Component<ICustomerOrderProps, ICustomerOrderState> {
  state: ICustomerOrderState = {
    searchStatus: '',
    searchOrderNumber: '',
    searchTabOpen: false,
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (
      this.state.searchStatus ||
      this.state.searchOrderNumber ||
      this.props.searchInvoiceNumber ||
      this.props.searchFromOrderDate ||
      this.props.searchToOrderDate
    ) {
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, searchStatus, searchOrderNumber } = this.state;
        const { searchFromOrderDate, searchToOrderDate, searchInvoiceNumber } = this.props;
        this.props.getSearchEntities(
          searchStatus,
          searchOrderNumber,
          searchInvoiceNumber,
          searchFromOrderDate,
          searchToOrderDate,
          activePage - 1,
          itemsPerPage,
          `${sort},${order}`
        );
      });
    }
  };

  clear = () => {
    this.props.searchFromOrderDateChanged('');
    this.props.searchToOrderDateChanged('');
    this.props.searchInvoiceNumberChanged('');
    this.setState(
      {
        searchStatus: '',
        searchOrderNumber: '',
        activePage: 1
      },
      () => {
        this.props.getEntities(0, ITEMS_PER_PAGE, 'id,desc');
      }
    );
  };

  handleSearchStatusChange = event => this.setState({ searchStatus: event.target.value });
  handleSearchOrderNumberChange = event => this.setState({ searchOrderNumber: event.target.value });
  handleSearchInvoiceNumberChange = event => this.props.searchInvoiceNumberChanged(event.target.value);
  handleSearchFromOrderDateChange = event => this.props.searchFromOrderDateChanged(event.target.value);
  handleSearchToOrderDateChange = event => this.props.searchToOrderDateChanged(event.target.value);

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order, searchStatus, searchOrderNumber } = this.state;
    const { searchFromOrderDate, searchToOrderDate, searchInvoiceNumber } = this.props;
    if (searchStatus || searchOrderNumber || searchInvoiceNumber || searchFromOrderDate || searchToOrderDate) {
      this.props.getSearchEntities(
        searchStatus,
        searchOrderNumber,
        searchInvoiceNumber,
        searchFromOrderDate,
        searchToOrderDate,
        activePage - 1,
        itemsPerPage,
        `${sort},${order}`
      );
    } else {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  clickDateRangeThisMonth = event => {
    const start = moment()
      .startOf('month')
      .format('YYYY-MM-DD');
    const end = moment()
      .endOf('month')
      .format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeLastMonth = event => {
    const start = moment()
      .subtract(1, 'month')
      .startOf('month')
      .format('YYYY-MM-DD');
    const end = moment()
      .subtract(1, 'month')
      .endOf('month')
      .format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeThisYear = event => {
    const start = moment()
      .startOf('year')
      .format('YYYY-MM-DD');
    const end = moment()
      .endOf('year')
      .format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeLastYear = event => {
    const start = moment()
      .subtract(1, 'year')
      .startOf('year')
      .format('YYYY-MM-DD');
    const end = moment()
      .subtract(1, 'year')
      .endOf('year')
      .format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeThisFY = event => {
    const now = moment();
    let start;
    let end;
    if (now.month() >= 3) {
      start = moment()
        .month(3)
        .startOf('month')
        .format('YYYY-MM-DD');
      end = moment()
        .add(1, 'year')
        .month(2)
        .endOf('month')
        .format('YYYY-MM-DD');
    } else {
      start = moment()
        .subtract(1, 'year')
        .month(3)
        .startOf('month')
        .format('YYYY-MM-DD');
      end = moment()
        .month(2)
        .endOf('month')
        .format('YYYY-MM-DD');
    }

    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeLastFY = event => {
    const now = moment();
    let start;
    let end;
    if (now.month() >= 3) {
      start = moment()
        .subtract(1, 'year')
        .month(3)
        .startOf('month')
        .format('YYYY-MM-DD');
      end = moment()
        .subtract(2)
        .endOf('month')
        .format('YYYY-MM-DD');
    } else {
      start = moment()
        .subtract(2, 'year')
        .month(3)
        .startOf('month')
        .format('YYYY-MM-DD');
      end = moment()
        .subtract(1, 'year')
        .month(2)
        .endOf('month')
        .format('YYYY-MM-DD');
    }
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangeLast12Months = event => {
    const start = moment()
      .subtract(1, 'year')
      .add(1, 'day')
      .format('YYYY-MM-DD');
    const end = moment().format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  clickDateRangePrevious12Months = event => {
    const start = moment()
      .subtract(2, 'year')
      .add(1, 'day')
      .format('YYYY-MM-DD');
    const end = moment()
      .subtract(1, 'year')
      .format('YYYY-MM-DD');
    this.props.searchFromOrderDateChanged(start);
    this.props.searchToOrderDateChanged(end);
  };

  toggleSearchTab = () => {
    if (this.searchFieldsPresent() || this.state.searchTabOpen) {
      this.clear();
      this.setState({ searchTabOpen: false });
    } else {
      this.setState({ searchTabOpen: true });
    }
  };

  searchFieldsPresent(): boolean {
    return !!(
      this.state.searchStatus ||
      this.state.searchOrderNumber ||
      this.props.searchInvoiceNumber ||
      this.props.searchFromOrderDate ||
      this.props.searchToOrderDate
    );
  }

  render() {
    const { customerOrderList, match, totalItems, sumSubTotals, sumTotals } = this.props;
    return (
      <div>
        <h2 id="customer-order-heading">Customer Orders</h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <Button color="primary" onClick={this.toggleSearchTab} style={{ marginBottom: '1rem' }}>
                {this.searchFieldsPresent() || this.state.searchTabOpen ? 'Filter <<' : 'Filter >>'}
              </Button>
              <Collapse isOpen={this.searchFieldsPresent() || this.state.searchTabOpen}>
                <Card>
                  <CardBody>
                    <Container>
                      <Row>
                        <Col>
                          <Row>
                            <Col>
                              <span>Invoice Number</span>

                              <AvInput
                                type="text"
                                name="searchInvoiceNumber"
                                onChange={this.handleSearchInvoiceNumberChange}
                                value={this.props.searchInvoiceNumber}
                                className="m-1"
                              />
                            </Col>
                          </Row>
                          <Row>
                            <Col>
                              <span>status: </span>
                              <AvInput
                                id="search-status"
                                type="select"
                                name="status"
                                className="m-1"
                                value={this.state.searchStatus}
                                onChange={this.handleSearchStatusChange}
                              >
                                <option value="" />
                                {/* no filter*/}
                                <option value="PLACED">Awaiting Despatch</option>
                                {/*no despatch_date*/}
                                <option value="DESPATCHED">Awaiting Invoice</option>
                                {/*no invoice_date*/}
                                <option value="INVOICED">Awaiting Payment</option>
                                {/*no payment_date*/}
                                <option value="PAID">Completed</option>
                                {/*despatch_date, invoice_date, payment_date set*/}
                              </AvInput>
                            </Col>
                          </Row>
                          <Row>
                            <Col>
                              <span>From order date: </span>
                              <AvField
                                id="searchFromOrderDate"
                                type="date"
                                name="searchFromOrderDate"
                                onChange={this.handleSearchFromOrderDateChange}
                                value={this.props.searchFromOrderDate}
                                className="m-1"
                              />
                            </Col>
                            <Col>
                              <span>To order date: </span>
                              <AvField
                                id="searchToOrderDate"
                                type="date"
                                name="searchToOrderDate"
                                onChange={this.handleSearchToOrderDateChange}
                                value={this.props.searchToOrderDate}
                                className="m-1"
                              />
                            </Col>
                          </Row>
                          <Button color="primary" size="sm" onClick={this.clickDateRangeThisMonth}>
                            This Month
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeLastMonth}>
                            Last Month
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeThisYear}>
                            This Year
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeLastYear}>
                            Last Year
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeThisFY}>
                            This FY
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeLastFY}>
                            Last FY
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangeLast12Months}>
                            Last 12 Months
                          </Button>{' '}
                          <Button color="primary" size="sm" onClick={this.clickDateRangePrevious12Months}>
                            Previous 12 Months
                          </Button>{' '}
                        </Col>
                      </Row>

                      <Row>
                        <div className="mt-2">
                          <Button className="input-group-addon mx-1">
                            <FontAwesomeIcon icon="search" />
                          </Button>
                          <Button type="reset" className="input-group-addon" onClick={this.clear}>
                            <FontAwesomeIcon icon="trash" />
                          </Button>
                        </div>
                        <Container className="mt-3">
                          <Col>
                            <b>{totalItems}</b> <span>Records</span>
                          </Col>
                          <Col>
                            {sumSubTotals ? (
                              <div>
                                <span>Total Pre VAT:</span>{' '}
                                <b>
                                  £
                                  {sumSubTotals.toLocaleString(undefined, {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                  })}
                                </b>
                              </div>
                            ) : (
                              <div />
                            )}
                          </Col>
                          <Col>
                            {sumTotals ? (
                              <div>
                                <span>Total Post VAT:</span>{' '}
                                <b>
                                  £
                                  {sumTotals.toLocaleString(undefined, {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                  })}
                                </b>
                              </div>
                            ) : (
                              <div />
                            )}
                          </Col>
                        </Container>
                      </Row>
                    </Container>
                  </CardBody>
                </Card>
              </Collapse>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('orderNumber')}>
                  Order Number <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Customer <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('orderDate')}>
                  Order Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('despatchDate')}>
                  Despatch Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('invoiceDate')}>
                  Invoice Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('paymentDate')}>
                  Payment Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('invoiceNumber')}>
                  Invoice Number <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand">Total</th>

                <th />
              </tr>
            </thead>
            <tbody>
              {customerOrderList.map((customerOrder, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerOrder.id}`} color="link" size="sm">
                      {customerOrder.orderNumber}
                    </Button>
                  </td>
                  <td>
                    {customerOrder.customerId ? (
                      <Link
                        to={
                          customerOrder.customerType === 'DOMESTIC'
                            ? `domestic/${customerOrder.customerId}`
                            : `dampproofer/${customerOrder.customerId}`
                        }
                      >
                        {customerOrder.customerName}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    <TextFormat type="date" value={customerOrder.orderDate} format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
                  </td>
                  <td>
                    <TextFormat type="date" value={customerOrder.despatchDate} format={APP_LOCAL_DATE_FORMAT} blankOnInvalid />
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
                      ? customerOrder.total.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
                      : null}
                  </td>

                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerOrder.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ customerOrder }: IRootState) => ({
  customerOrderList: customerOrder.entities,
  totalItems: customerOrder.totalItems,
  sumSubTotals: customerOrder.sumSubTotals,
  sumTotals: customerOrder.sumTotals,
  searchFromOrderDate: customerOrder.searchFromOrderDate,
  searchToOrderDate: customerOrder.searchToOrderDate,
  searchInvoiceNumber: customerOrder.searchInvoiceNumber
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
  searchFromOrderDateChanged,
  searchToOrderDateChanged,
  searchInvoiceNumberChanged
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrder);
