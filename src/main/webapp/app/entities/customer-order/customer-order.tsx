import React from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Container, InputGroup, Row, Table } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { getPaginationItemsNumber, IPaginationBaseState, JhiPagination, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getSortState } from 'app/shared/util/dryhome-pagination-utils';
import { getSearchEntities, getEntities } from './customer-order.reducer';
// tslint:disable-next-line:no-unused-variable
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';

export interface ICustomerOrderProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICustomerOrderState extends IPaginationBaseState {
  searchStatus: string;
  searchOrderNumber: string;
  searchInvoiceNumber: string;
  searchOrderDate: string;
}

export class CustomerOrder extends React.Component<ICustomerOrderProps, ICustomerOrderState> {
  state: ICustomerOrderState = {
    searchStatus: '',
    searchOrderNumber: '',
    searchInvoiceNumber: '',
    searchOrderDate: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.searchStatus || this.state.searchOrderNumber || this.state.searchInvoiceNumber || this.state.searchOrderDate) {
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, searchStatus, searchOrderNumber, searchInvoiceNumber, searchOrderDate } = this.state;
        this.props.getSearchEntities(
          searchStatus,
          searchOrderNumber,
          searchInvoiceNumber,
          searchOrderDate,
          activePage - 1,
          itemsPerPage,
          `${sort},${order}`
        );
      });
    }
  };

  clear = () => {
    this.setState(
      {
        searchStatus: '',
        searchOrderNumber: '',
        searchInvoiceNumber: '',
        searchOrderDate: '',
        activePage: 1
      },
      () => {
        this.props.getEntities(0, ITEMS_PER_PAGE, 'id,desc');
      }
    );
  };

  handleSearchStatusChange = event => this.setState({ searchStatus: event.target.value });
  handleSearchOrderNumberChange = event => this.setState({ searchOrderNumber: event.target.value });
  handleSearchInvoiceNumberChange = event => this.setState({ searchInvoiceNumber: event.target.value });
  handleSearchOrderDateChange = event => this.setState({ searchOrderDate: event.target.value });

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
    const { activePage, itemsPerPage, sort, order, searchStatus, searchOrderNumber, searchInvoiceNumber, searchOrderDate } = this.state;
    if (searchStatus || searchOrderNumber || searchInvoiceNumber || searchOrderDate) {
      this.props.getSearchEntities(
        searchStatus,
        searchOrderNumber,
        searchInvoiceNumber,
        searchOrderDate,
        activePage - 1,
        itemsPerPage,
        `${sort},${order}`
      );
    } else {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { customerOrderList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="customer-order-heading">Customer Orders</h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search} className="border">
              <span>Search</span>
              <AvGroup>
                <Container>
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
                        <option value="AWAITING_DESPATCH">Awaiting Despatch</option>
                        {/*no despatch_date*/}
                        <option value="AWAITING_INVOICE">Awaiting Invoice</option>
                        {/*no invoice_date*/}
                        <option value="AWAITING_PAYMENT">Awaiting Payment</option>
                        {/*no payment_date*/}
                        <option value="COMPLETED">Completed</option>
                        {/*despatch_date, invoice_date, payment_date set*/}
                      </AvInput>
                      <span>order date: </span>
                      <AvField
                        id="searchOrderDate"
                        type="date"
                        name="searchOrderDate"
                        onChange={this.handleSearchOrderDateChange}
                        value={this.state.searchOrderDate}
                        className="m-1"
                      />
                    </Col>
                    <Col>
                      <span>Order Number: </span>
                      <AvInput
                        type="text"
                        name="searchOrderNumber"
                        value={this.state.searchOrderNumber}
                        onChange={this.handleSearchOrderNumberChange}
                        placeholder="Order Number"
                        className="m-1"
                      />
                      <span>Invoice Number: </span>

                      <AvInput
                        type="text"
                        name="searchInvoiceNumber"
                        value={this.state.searchInvoiceNumber}
                        onChange={this.handleSearchInvoiceNumberChange}
                        placeholder="Invoice Number"
                        className="m-1"
                      />
                    </Col>
                  </Row>

                  <Button className="input-group-addon mx-1">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                  <Row>
                    <span>{totalItems} Records</span>
                  </Row>
                </Container>
              </AvGroup>
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
  totalItems: customerOrder.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrder);
