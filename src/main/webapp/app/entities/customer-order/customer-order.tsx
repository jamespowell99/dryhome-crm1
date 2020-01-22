import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getSortState } from 'app/shared/util/dryhome-pagination-utils';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './customer-order.reducer';
import { ICustomerOrder } from 'app/shared/model/customer-order.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICustomerOrderProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICustomerOrderState extends IPaginationBaseState {
  search: string;
}

export class CustomerOrder extends React.Component<ICustomerOrderProps, ICustomerOrderState> {
  state: ICustomerOrderState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, search } = this.state;
        this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
      });
    }
  };

  clear = () => {
    this.setState({ search: '', activePage: 1 }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

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
    const { activePage, itemsPerPage, sort, order, search } = this.state;
    if (search) {
      this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
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
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
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
                    {/*todo could be dp or dom*/}
                    {customerOrder.customerId ? (
                      <Link to={`dampproofer/${customerOrder.customerId}`}>{customerOrder.customerName}</Link>
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
                      <Button tag={Link} to={`${match.url}/${customerOrder.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
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
