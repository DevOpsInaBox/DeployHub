/*
 *  DeployHub is an Agile Application Release Automation Solution
 *  Copyright (C) 2017 Catalyst Systems Corporation DBA OpenMake Software
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
#ifndef __repositories_http_h

#define __repositories_http_h


#include "repository.h"


///////////////////////////////////////////////////////////////////////////////
// HttpRepository

class HttpRepositoryImpl : public virtual RepositoryImpl
{
private:
	char	*m_host;
	int		m_port;
	char	*m_uri;
	char	*m_params;
	char	*m_version;
	char	*m_logfile;
	bool	m_secure;
	bool	CheckFileExists(int sock,char *AuthenticationString,char *offset, char *patt);
	char	**GetDirectoryContent(Context &ctx,/*int sock,*/char *AuthenticationString,char *offset,int *numFiles, FILE *logout);
	void	FreeDirectoryContent(char **dirlist, int numElements);

public:
	HttpRepositoryImpl(
		class RepositoryImplFactory &factory, DMINT32 implId, class Repository &repository,
		const char *host, int port, bool ssl, const char *uri, const char *params, const char *version, const char *logfile);

	virtual ~HttpRepositoryImpl();

	bool testProvider(class Context &ctx);

	void checkout(
		const char *dzbase, const char *dzpath, bool recursive,
		class StringList *pattern, class ExtendedStmt &stmt,
		class DropzoneCallback &callback, class Context &ctx);

	void checkin(
		const char *dzbase, const char *dzpath, class ExtendedStmt &stmt,
		class DropzoneCallback &callback, class Context &ctx);

	char *dirname(const char *filename);

	// RepositoryImpl
	class Expr *getAttribute(const char *name, class Context &ctx);
};


class HttpRepositoryImplFactory : public virtual RepositoryImplFactory
{
public:
	HttpRepositoryImplFactory();

	RepositoryImpl *create(
		DMINT32 implId, class Repository &parent, class ExtendedStmt &stmt,
		class Context &ctx);
};


#endif /*__repositories_http_h*/
