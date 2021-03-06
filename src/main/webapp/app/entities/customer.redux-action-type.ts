import { AxiosPromise } from 'axios';

export interface IPayload<T> {
  type: string;
  payload: AxiosPromise<T>;
  meta?: any;
}
// export type IPayloadResult<T> = ((dispatch: any) => IPayload<T> | Promise<IPayload<T>>);
// export type ICrudGetAction<T> = (id: string | number) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudGetAllCustomerAction<T> = (
  type?: string,
  page?: number,
  size?: number,
  sort?: string
) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudSearchCustomerAction<T> = (
  searchId?: string,
  searchCompanyName?: string,
  searchTown?: string,
  searchPostCode?: string,
  searchLastName?: string,
  searchTel?: string,
  searchMob?: string,
  type?: string,
  page?: number,
  size?: number,
  sort?: string
) => IPayload<T> | ((dispatch: any) => IPayload<T>);

export type ICrudGetOrdersByCustomerIdAction<T> = (
  customerId?: string | number,
  page?: number,
  size?: number
) => IPayload<T> | ((dispatch: any) => IPayload<T>);

// export type ICrudPutAction<T> = (data?: T) => IPayload<T> | IPayloadResult<T>;
// export type ICrudDeleteAction<T> = (id?: string | number) => IPayload<T> | IPayloadResult<T>;
