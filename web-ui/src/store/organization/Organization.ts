import {Organization} from "../define.ts";

export interface OrganizationAction {
    selectOrganization: (organization: Organization | undefined) => void;
}